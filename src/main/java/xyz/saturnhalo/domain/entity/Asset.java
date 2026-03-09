package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.saturnhalo.enums.StorageType;

import java.time.LocalDateTime;

/**
 * 文件资产表
 */
@Data
@Entity
@Table(name = "assets")
public class Asset {

    /**
     * 文件摘要值（使用 SHA256）
     */
    @Id
    @Column(length = 64, nullable = false, updatable = false)
    private String hash;

    /**
     * 文件大小
     */
    @Column(nullable = false)
    private Long size;

    /**
     * 存储方式
     */
    @Column(name = "storage_type")
    private StorageType storageType;

    /**
     * 存储路径
     */
    @Column(name = "storage_path", length = 500)
    private String storagePath;

    /**
     * 引用计数
     */
    @Column(name = "reference_count", nullable = false)
    private Integer referenceCount;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}