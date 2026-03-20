package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import xyz.saturnhalo.enums.StorageType;

import java.time.LocalDateTime;

/**
 * 文件资产表
 */
@Data
@TableName(value = "assets")
public class Asset {

    /**
     * 文件摘要值（使用 SHA256）
     */
    @TableId(type = IdType.INPUT)
    private String hash;

    /**
     * 文件大小
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 存储方式
     */
    @TableField(value = "storage_type")
    private StorageType storageType;

    /**
     * 存储路径
     */
    @TableField(value = "storage_path")
    private String storagePath;

    /**
     * 引用计数
     */
    @TableField(value = "reference_count")
    private Integer referenceCount;

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
     * 乐观锁版本号
     */
    @Version
    @TableField(value = "version")
    private Integer version;
}