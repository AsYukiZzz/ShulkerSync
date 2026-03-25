package xyz.saturnhalo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.saturnhalo.domain.vo.CheckResult;
import xyz.saturnhalo.domain.vo.MergeResult;
import xyz.saturnhalo.exception.BusinessException;
import xyz.saturnhalo.properties.StorageProps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * 整合包上传策略：本地上传
 */
@Slf4j
@Service
public class LocalUploadServiceImpl {

    @Autowired
    private StorageProps storageProps;

    /**
     * 上传前预检
     */
    public CheckResult uploadCheck(String slug, String hash, Integer totalChunk) {
        log.info("开始处理整合包(slug={})文件上传预检请求，文件哈希值={}，分片数量={}", slug, hash, totalChunk);

        // 1. 映射上传文件夹对象
        File chunkFolder = new File(storageProps.getTempDir() + File.separator + hash);

        // 2. 检验文件夹是否存在
        if (!chunkFolder.exists() || !chunkFolder.isDirectory()) {
            // 2.1 文件夹不存在：说明文件还未被上传
            log.info("文件(hash={})未上传", hash);
            return CheckResult.builder()
                    .status("NOT_UPLOADED")
                    .build();
        }

        // 2.2 文件夹存在：返回给前端已上传的文件列表
        List<Integer> chunkFiles;
        try (Stream<Path> pathStream = Files.list(chunkFolder.toPath())) {

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
    public void uploadChunk(String slug, String modpackHash, MultipartFile file, String chunkHash, Integer chunkIndex) {
        log.info("开始处理整合包(slug={})文件分片上传请求，文件分片序列号={}", slug, chunkIndex);

        try {
            // 1. 分片文件哈希值校验
            String actualChunkHash = DigestUtils.sha256Hex(file.getInputStream());
            if (!actualChunkHash.equalsIgnoreCase(chunkHash)) {
                log.warn("文件(slug={},chunkIndex={})分片哈希值校验不符，疑似损坏，前端计算哈希值={},后端计算哈希值={}", slug, chunkIndex, chunkHash, actualChunkHash);
                throw new BusinessException("文件分片数据损坏");
            }

            // 2. 将文件分片保存到临时目录中
            Path chunkDirPath = Path.of(storageProps.getTempDir(), modpackHash);
            if (!Files.exists(chunkDirPath)) {
                Files.createDirectories(chunkDirPath);
            }

            Path chunkFilePath = chunkDirPath.resolve(String.valueOf(chunkIndex));
            file.transferTo(chunkFilePath.toFile());

        } catch (IOException e) {
            log.error("读取文件时发生错误", e);
            throw new BusinessException("读取文件时发生错误，请联系管理员进行处理");
        }
    }

    /**
     * 合并分片与发布版本
     */
    public MergeResult uploadMerge(String slug, String modpackHash, String fileName, Integer totalChunk) {
        log.info("开始处理整合包(slug={})文件分片合并请求，分片数量={}", slug, totalChunk);

        // 1. 检查临时分片目录是否存在
        File chunkFolder = new File(storageProps.getTempDir() + File.separator + modpackHash);
        if (!chunkFolder.exists()) {
            log.warn("找不到临时分片目录={}", modpackHash);
            throw new BusinessException("对应临时分片目录不存在，请重新上传");
        }

        // 2. 设置合并文件输出路径 (Temp 文件夹根目录)
        String mergedFileName = modpackHash + "_" + fileName;
        File outputFile = new File(storageProps.getTempDir() + File.separator + mergedFileName);

        // 3. 合并分片文件 (NIO 零拷贝)
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             FileChannel outChannel = fos.getChannel()) {

            for (int i = 0; i < totalChunk; i++) {
                File chunkFile = new File(chunkFolder, String.valueOf(i));
                if (!chunkFile.exists()) {
                    log.warn("文件合片失败：缺少分片 {}", i);
                    throw new BusinessException("文件合片失败：缺少分片 " + i);
                }

                try (FileInputStream fis = new FileInputStream(chunkFile);
                     FileChannel inChannel = fis.getChannel()) {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                }
            }

        } catch (IOException e) {
            log.error("整合包文件(slug={}, hash={})分片合并失败", slug, modpackHash, e);
            throw new BusinessException("文件合并失败，请联系管理员进行处理");
        }

        // 4. 校验完整文件哈希
        try (FileInputStream hashFis = new FileInputStream(outputFile)) {
            String actualHash = DigestUtils.sha256Hex(hashFis);

            if (!actualHash.equalsIgnoreCase(modpackHash)) {
                log.warn("文件(slug={})哈希校验失败，预期={}, 实际={}", slug, modpackHash, actualHash);
                hashFis.close();
                outputFile.delete();
                throw new BusinessException("文件完整性校验失败，数据可能已损坏，请重新上传");
            }
        } catch (IOException e) {
            log.error("计算文件(slug={}) SHA-256 期间发生 IO 异常", slug, e);
            throw new BusinessException("哈希校验过程异常，请联系管理员进行处理");
        }

        // 5. 清理临时分片文件
        cleanUpTempDir(chunkFolder);
        log.info("文件合并且校验成功，暂存路径: {}", outputFile.getAbsolutePath());

        return MergeResult.builder()
                .filePath(outputFile.getAbsolutePath())
                .build();
    }

    /**
     * 检查目标是否为文件
     */
    private boolean isValidChunkFile(Path path) {
        // 1. 检查是否为文件
        if (!Files.isRegularFile(path)) {
            return false;
        }
        try {
            // 2. 检查文件大小是否大于 0
            return Files.size(path) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 递归删除临时分片文件夹
     */
    private void cleanUpTempDir(File chunkDir) {
        File[] files = chunkDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        chunkDir.delete();
    }
}