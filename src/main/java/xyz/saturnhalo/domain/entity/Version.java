package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.saturnhalo.anno.CustomIdPolicy;

import java.time.LocalDateTime;

/**
 * 整合包版本 Entity
 */
@Data
@Entity
@Table(
        name = "versions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"modpack_id", "modpack_version"})
        })
public class Version {

    /**
     * 主键 Id
     */
    @Id
    @CustomIdPolicy
    private Long id;

    /**
     * 整合包项目信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modpack_id", nullable = false)
    private Modpack modpack;

    /**
     * 整合包分支信息
     */
    @Column(length = 50, nullable = false)
    private String branch;

    /**
     * 整合包版本号
     */
    @Column(name = "modpack_version", length = 30, nullable = false)
    private String modpackVersion;

    /**
     * 对应游戏版本号
     */
    @Column(name = "minecraft_version", length = 30, nullable = false)
    private String minecraftVersion;

    /**
     * 整合包版本内容信息
     */
    @Lob
    @Column(name = "manifest_json", columnDefinition = "TEXT", nullable = false)
    private String manifestJson;

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