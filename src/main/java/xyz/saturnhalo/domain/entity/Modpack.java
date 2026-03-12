package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 整合包项目 Entity
 */
@Getter
@Setter
@TableName(value = "modpacks")
@NoArgsConstructor
public class Modpack {

    /**
     * 主键 ID
     */
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    /**
     * 唯一标识符
     */
    @TableField(value = "slug")
    private String slug;

    /**
     * 整合包名字
     */
    @TableField(value = "name")
    private String name;

    /**
     * 整合包描述
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