package xyz.saturnhalo.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签删除请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDeleteReq {

    /**
     * 标签 Id
     */
    @NotEmpty(message = "标签 Id 不能为空")
    private Long id;

    /**
     * 标签树 Id
     */
    @NotEmpty(message = "标签树 Id 不能为空")
    private Long treeId;

    /**
     * 标签树版本号
     */
    @NotEmpty(message = "标签树版本号不能为空")
    private Integer version;
}