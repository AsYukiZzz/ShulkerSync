package xyz.saturnhalo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件记录模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRecord {

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件哈希
     */
    private String hash;

    /**
     * 文件大小
     */
    private long size;
}