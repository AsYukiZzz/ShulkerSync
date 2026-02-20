package xyz.saturnhalo.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 整合包项目 Entity
 */
@Data
@Entity
@Table(name = "modpacks")
public class Modpack {

    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 整合包 URL
     */
    @Column(nullable = false, unique = true)
    private String slug;

    /**
     * 整合包名字
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 整合包描述
     */
    @Column(columnDefinition = "Text")
    private String description;

    /**
     * 整合包版本信息列表
     */
    @OneToMany(mappedBy = "modpack", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Version> versionList;

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