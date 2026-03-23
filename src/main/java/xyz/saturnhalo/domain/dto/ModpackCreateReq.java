package xyz.saturnhalo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 整合包创建请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModpackCreateReq {

    /**
     * 整合包唯一标识符
     */
    @NotBlank(message = "整合包标识去除空格后内容不应为空")
    private String slug;

    /**
     * 整合包名字
     */
    @NotBlank(message = "整合包名字取出空格后内容不应为空")
    private String name;

    /**
     * 整合包描述
     */
    private String description;

    /**
     * 整合包标签列表
     */
    private List<Long> tagIds;
}