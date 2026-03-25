package xyz.saturnhalo.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分片合并结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeResult {

    /**
     * 文件路径
     */
    private String filePath;
}