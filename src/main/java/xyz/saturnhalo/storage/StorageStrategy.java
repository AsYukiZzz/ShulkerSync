package xyz.saturnhalo.storage;

import java.io.InputStream;

/**
 * 分布式存储与分发策略顶层接口
 */
public interface StorageStrategy {

    /**
     * 判断指定文件是否存在
     */
    boolean exists(String fileHash);

    /**
     * 存储文件
     */
    String store(InputStream inputStream, String fileHash, long size);

    /**
     * 删除文件
     */
    boolean delete(String fileHash);

    /**
     * 获取文件下载链接
     */
    String getDownloadUrl(String fileHash);
}