package xyz.saturnhalo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签修改请求z
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagInfoUpdateReq {

    /**
     * 标签 ID
     */
    @NotEmpty(message = "标签 Id 不能为空")
    private Long id;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称去除空格后内容不应为空")
    @Size(max = 50, message = "标签名称长度超过 50 个字符的限制")
    private String name;

    /**
     * 标签颜色
     */
    @NotBlank(message = "标签颜色去除空格后内容不应为空")
    @Size(max = 7, message = "标签颜色长度不符合预设要求")
    private String color;

    /**
     * 标签自身版本号
     */
    @NotEmpty(message = "标签版本号不能为空")
    private Long version;
}