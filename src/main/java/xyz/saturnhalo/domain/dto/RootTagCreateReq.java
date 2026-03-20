package xyz.saturnhalo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根标签创建请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RootTagCreateReq {

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
}