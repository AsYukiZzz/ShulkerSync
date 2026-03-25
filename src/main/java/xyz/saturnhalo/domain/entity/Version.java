package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 整合包版本 Entity
 */
@Data
@TableName(value = "versions")
public class Version {

    /**
     * 主键 Id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 整合包 ID
     */
    @TableField(value = "modpack_id")
    private Long modpackId;

    /**
     * 整合包分支
     */
    @TableField(value = "branch")
    private String branch;

    /**
     * 版本发行说明
     */
    @TableField(value = "desc")
    private String desc;

    /**
     * 整合包版本号
     */
    @TableField(value = "modpack_version")
    private String modpackVersion;

    /**
     * 对应游戏版本号
     */
    @TableField(value = "minecraft_version")
    private String minecraftVersion;

    /**
     * 整合包文件清单
     */
    @TableField(value = "manifest_json")
    private String manifestJson;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 标签树版本号
     */
    @com.baomidou.mybatisplus.annotation.Version
    @TableField(value = "version")
    private Long version;
}