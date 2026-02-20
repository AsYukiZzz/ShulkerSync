package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 整合包版本 Entity
 */
@Data
@Entity
@Table(
        name = "versions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"modpack_id", "version_number"})
        })
public class Version {

    /**
     * 主键 Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 整合包项目信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modpack_id", nullable = false)
    private Modpack modpack;

    /**
     * 整合包版本号
     */
    @Column(nullable = false, name = "version_number")
    private String versionNumber;

    /**
     * 整合包版本内容信息
     */
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false, name = "manifest_json")
    private String manifestJson;

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