package xyz.saturnhalo.storage.impl;

import xyz.saturnhalo.storage.StorageStrategy;

import java.io.InputStream;

/**
 * 云端 S3 存储策略实现
 */
public class S3StorageServiceImpl implements StorageStrategy {
    @Override
    public boolean exists(String fileHash) {
        return false;
    }

    @Override
    public String store(InputStream inputStream, String fileHash, long size) {
        return "";
    }

    @Override
    public boolean delete(String fileHash) {
        return false;
    }

    @Override
    public String getDownloadUrl(String fileHash) {
        return "";
    }
}