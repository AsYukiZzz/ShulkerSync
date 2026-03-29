package xyz.saturnhalo.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import xyz.saturnhalo.exception.BusinessException;
import xyz.saturnhalo.properties.StorageProps;
import xyz.saturnhalo.strategy.StorageStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 本地 Tomcat 存储策略实现
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "shulkersync.storage", name = "active-strategy", havingValue = "local", matchIfMissing = true)
public class LocalTomcatStorageServiceImpl implements StorageStrategy {

    @Autowired
    private StorageProps storageProps;

    /**
     * 判断指定文件是否存在
     */
    @Override
    public boolean exists(String fileHash) {
        // 1. 返回文件是否存在的布尔值
        return Files.exists(getFilePath(fileHash));
    }

    /**
     * 存储文件
     */
    @Override
    public String store(InputStream inputStream, String fileHash, long size) {
        // 1. 获取文件夹路径
        Path targetDir = getDirPath(fileHash);
        Path targetFile = targetDir.resolve(fileHash);

        try {
            // 1. 确保文件夹存在
            Files.createDirectories(targetDir);

            // 2. 物理层去重检查
            if (Files.exists(targetFile)) {
                log.debug("文件存储命中去重，文件名字={}，存储策略={Local-Tomcat}", fileHash);
                return targetFile.toString();
            }

            // 3. 创建临时文件
            String tempFileName = fileHash + ".tmp" + UUID.randomUUID();
            Path tempFile = targetDir.resolve(tempFileName);

            try {
                // 4. 写入临时文件
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

                // 5. 变更文件名字（原子操作）
                Files.move(tempFile, targetFile, StandardCopyOption.ATOMIC_MOVE);

            } catch (Exception e) {

                // 6. 落盘失败清除临时文件
                Files.deleteIfExists(tempFile);
                throw e;
            }

            return targetFile.toString();

        } catch (IOException e) {
            log.error("文件存储失败，文件名字={}，存储策略={Local-Tomcat}", fileHash, e);
            throw new BusinessException("本地存储写入异常");
        }
    }

    /**
     * 删除文件
     */
    @Override
    public boolean delete(String fileHash) {
        try {
            return Files.deleteIfExists(getFilePath(fileHash));
        } catch (IOException e) {
            log.error("文件删除失败，文件名字={}，存储策略={Local-Tomcat}", fileHash, e);
        }
        return false;
    }

    /**
     * 获取文件下载链接
     */
    @Override
    public String getDownloadUrl(String fileHash) {
        return "/download/assets/" + fileHash;
    }

    /**
     * 获取文件存储的路由路径
     */
    private Path getDirPath(String fileHash) {
        // 1. 获取文件名的 1-2 位与 3-4 位
        String level1 = fileHash.substring(0, 2);
        String level2 = fileHash.substring(2, 4);

        // 2. 获取对应文件夹的 Path 对象
        return Path.of(storageProps.getAssetsDir(), level1, level2);
    }

    /**
     * 获取文件存储路径
     */
    private Path getFilePath(String fileHash) {
        // 1. 获取文件的路由路径
        Path routePath = getDirPath(fileHash);

        // 2. 构造完整的文件路径
        return routePath.resolve(fileHash);
    }
}