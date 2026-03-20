package xyz.saturnhalo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 子标签创建请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildTagCreateReq {

    /**
     * 父标签 Id
     */
    @NotEmpty(message = "父标签 Id 不能为空")
    private Long parentId;

    /**
     * 标签树 Id
     */
    @NotEmpty(message = "标签树 Id 不能为空")
    private Long treeId;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称去除空格后内容不应为空")
    @Size(max = 50, message = "标签名称长度超过 50 个字符的限制")
    private String name;

    /**
     * 标签颜色
     */
    @Size(max = 7, message = "标签颜色长度不符合预设要求")
    private String color;

    /**
     * 标签树版本号
     */
    @NotEmpty(message = "标签树版本号不能为空")
    private Integer version;
}