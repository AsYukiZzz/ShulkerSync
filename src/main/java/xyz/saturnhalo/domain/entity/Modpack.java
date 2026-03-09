package xyz.saturnhalo.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 整合包项目 Entity
 */
@Getter
@Setter
@Entity
@Table(
        name = "modpacks",
        indexes = {
                @Index(name = "idx_modpack_slug", columnList = "slug", unique = true)
        }
)
@NoArgsConstructor
public class Modpack {

    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 整合包唯一标识符
     */
    @Column(length = 100, nullable = false, unique = true)
    private String slug;

    /**
     * 整合包名字
     */
    @Column(length = 100, nullable = false, unique = true)
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
     * 整合包标签列表
     */
    @ManyToMany
    @JoinTable(
            name = "modpack_tag",
            joinColumns = @JoinColumn(name = "modpack_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


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