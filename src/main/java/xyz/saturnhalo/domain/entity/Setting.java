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
 * 系统配置项 Entity
 */
@Data
@TableName(value = "settings")
public class Setting {

    /**
     * 主键 Id
     */
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    /**
     * 配置项键
     */
    @TableField(value = "config_key")
    private String key;

    /**
     * 配置项值
     */
    @TableField(value = "config_value")
    private String value;

    /**
     * 配置项描述
     */
    @TableField(value = "description")
    private String description;

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