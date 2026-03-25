package xyz.saturnhalo.domain.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上传预检结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {

    /**
     * 上传状态
     * <p>
     *     1. NOT_UPLOADED：文件全部未上传
     * <p>
     *     2. PARTIAL_UPLOADED：文件上传一部分
     */
    private String status;

    /**
     * 已上传文件清单
     * <p>
     *     若上传状态为 PARTIAL_UPLOADED，则需告知前端已上传的分片，前端再上传时只需上传未上传的分片
     */
    private List<Integer> uploadedChunks;
}