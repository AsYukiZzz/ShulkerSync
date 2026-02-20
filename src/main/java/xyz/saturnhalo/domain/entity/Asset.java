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
     * 文件摘要值（SHA256）
     */
    @Id
    @Column(length = 64)
    private String hash;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 存储方式
     */
    private StorageType storageType;

    /**
     * 存储路径
     */
    @Column(length = 500)
    private String storagePath;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}