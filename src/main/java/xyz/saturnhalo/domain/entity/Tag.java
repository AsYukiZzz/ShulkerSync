package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签 Entity
 */
@Data
@TableName(value = "tags")
public class Tag {

    /**
     * 主键 Id
     */
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父标签 Id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 标签路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 标签名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 标签颜色
     */
    @TableField(value = "color")
    private String color;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Version
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}