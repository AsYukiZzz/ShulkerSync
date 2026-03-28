package xyz.saturnhalo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import xyz.saturnhalo.domain.vo.CheckResult;
import xyz.saturnhalo.exception.BusinessException;
import xyz.saturnhalo.properties.StorageProps;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 整合包上传策略：本地上传
 */
@Slf4j
@Service
public class LocalUploadServiceImpl {

    @Autowired
    private StorageProps storageProps;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 合并分片文件
     */
    private static void mergeChunkFiles(String slug, String modpackHash, Integer totalChunk, Path mergedFile, Path chunkFolder) {
        try (FileChannel outChannel = FileChannel.open(mergedFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            // 1. 合并分片文件
            for (int i = 0; i < totalChunk; i++) {
                Path chunkFile = chunkFolder.resolve(String.valueOf(i));
                if (Files.notExists(chunkFile)) {
                    log.warn("文件合片失败：缺少分片 {}", i);
                    throw new BusinessException("文件合片失败：缺少分片 " + i);
                }

                try (FileChannel inChannel = FileChannel.open(chunkFile, StandardOpenOption.READ)) {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                }
            }

        } catch (IOException e) {
            log.error("整合包文件(slug={}, hash={})分片合并失败", slug, modpackHash, e);
            throw new BusinessException("文件合并失败，请联系管理员进行处理");
        }
    }

    /**
     * 验证文件完整性（通过哈希）
     */
    private static void verifyFileHashOrThrow(String targetHash, Path file) {
        boolean isValid;
        try (InputStream inputStream = Files.newInputStream(file)) {

            // 1. 计算文件哈希
            String actualHash = DigestUtils.sha256Hex(inputStream);

            // 2. 验证文件哈希
            isValid = actualHash.equalsIgnoreCase(targetHash);

        } catch (IOException e) {
            log.error("计算文件(path={}) SHA-256 期间发生 IO 异常", file.toAbsolutePath());
            throw new BusinessException("哈希校验过程异常，请联系管理员进行处理");
        }

        if (!isValid) {

            // 2.1 哈希校验不匹配：清理损坏文件
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                log.error("损毁的文件(path={})清除失败", file.toAbsolutePath());
            }

            log.warn("文件(path={})哈希校验失败", file.toAbsolutePath());
            throw new BusinessException("文件完整性校验失败，数据可能已损坏，请重新上传");
        }
    }

    /**
     * 清理分片文件夹及其文件
     */
    private static void cleanChunkFolder(Path chunkFolder) {
        try {
            FileSystemUtils.deleteRecursively(chunkFolder);
        } catch (IOException e) {
            log.error("删除临时文件(path={})失败", chunkFolder.toAbsolutePath(), e);
        }
    }

    /**
     * 检查目标是否为有效文件
     */
    private boolean isValidChunkFile(Path file) {
        try {
            return Files.isRegularFile(file) && Files.size(file) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 上传前预检
     */
    public CheckResult uploadCheck(String slug, String hash, Integer totalChunk) {
        log.info("开始处理整合包(slug={})文件上传预检请求，文件哈希值={}，分片数量={}", slug, hash, totalChunk);

        // 1. 映射上传文件夹对象
        Path chunkFolder = Path.of(storageProps.getTempDir(), hash);

        // 2. 检验文件夹是否存在
        if (Files.notExists(chunkFolder) || !Files.isDirectory(chunkFolder)) {
            // 2.1 文件夹不存在：说明文件还未被上传
            log.info("文件(hash={})未上传", hash);
            return CheckResult.builder()
                    .status("NOT_UPLOADED")
                    .build();
        }

        // 2.2 文件夹存在：返回给前端已上传的文件列表
        List<Integer> chunkFiles;
        try (Stream<Path> pathStream = Files.list(chunkFolder)) {

            // 2.2.1 获取已上传文件列表
            chunkFiles = pathStream
                    .filter(this::isValidChunkFile)
                    .map(path -> Integer.parseInt(path.getFileName().toString()))
                    .toList();

        } catch (IOException e) {
            log.error("读取文件时发生错误", e);
            throw new BusinessException("读取文件时发生错误，请联系管理员进行处理");
        }

        return CheckResult.builder()
                .status("PARTIAL_UPLOADED")
                .uploadedChunks(chunkFiles)
                .build();
    }

    /**
     * 上传分片文件
     */
    public void uploadChunk(String slug, String fileHash, MultipartFile file, String chunkFileHash, Integer chunkIndex) {
        log.info("开始处理整合包(slug={})文件分片上传请求，文件分片序列号={}", slug, chunkIndex);

        try {

            // 1. 存储文件到临时目录中
            Path chunkFileDir = Path.of(storageProps.getTempDir(), fileHash);
            if (!Files.exists(chunkFileDir)) {
                Files.createDirectories(chunkFileDir);
            }
            Path chunkFile = chunkFileDir.resolve(String.valueOf(chunkIndex));
            file.transferTo(chunkFile);

            // 2. 对分片文件进行哈希校验
            verifyFileHashOrThrow(chunkFileHash, chunkFile);

        } catch (IOException e) {
            log.error("读取文件时发生错误", e);
            throw new BusinessException("读取文件时发生错误，请联系管理员进行处理");
        }
    }

    /**
     * 合并分片与发布版本
     */
    public void uploadMerge(String slug, String modpackHash, String fileName, Integer totalChunk) {
        log.info("开始处理整合包(slug={})文件分片合并请求，分片数量={}", slug, totalChunk);

        String safeFileName = FilenameUtils.getName(fileName);
        String mergedFileName = modpackHash + "_" + safeFileName;
        Path mergedFile = Path.of(storageProps.getTempDir(), mergedFileName);

        // 1. 检查文件分片是否已经被合并
        if (Files.exists(mergedFile)) {
            // 1.1 存在合并后的文件 -> 进行完整性校验
            verifyFileHashOrThrow(modpackHash, mergedFile);

            // 1.2 通过完整性校验则直接返回
            return;
        }

        // 2. 获取分布式锁
        RLock lock = redissonClient.getLock("merge:lock:" + modpackHash);
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(0, -1, TimeUnit.SECONDS);
            if (!isLocked) {
                log.warn("文件(hash={})正在合并中，拒绝其他并发合并请求", modpackHash);
                throw new BusinessException("文件正在合并处理中，请耐心等待，勿重复操作");
            }

            // 3. 再次检查文件分片是否已经被合并
            if (Files.exists(mergedFile)) {
                // 3.1 存在合并后的文件 -> 进行完整性校验
                verifyFileHashOrThrow(modpackHash, mergedFile);

                // 3.2 通过完整性校验则直接返回
                return;
            }

            // 4. 检查临时分片目录是否存在
            Path chunkFolder = Path.of(storageProps.getTempDir(), modpackHash);
            if (Files.notExists(chunkFolder)) {
                log.warn("找不到临时分片目录={}", modpackHash);
                throw new BusinessException("对应临时分片目录不存在，请重新上传");
            }

            // 5. 合并分片文件
            mergeChunkFiles(slug, modpackHash, totalChunk, mergedFile, chunkFolder);

            // 6. 校验完整文件哈希
            verifyFileHashOrThrow(modpackHash, mergedFile);

            // 7. 清理临时分片文件
            cleanChunkFolder(chunkFolder);

            log.info("文件合并且校验成功，暂存路径: {}", mergedFile.toAbsolutePath());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("系统处理被中断，请重试");
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}