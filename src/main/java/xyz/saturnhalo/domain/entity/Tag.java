package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import xyz.saturnhalo.anno.CustomIdPolicy;

/**
 * 标签 Entity
 */
@Data
@Entity
@Table(name = "tags")
public class Tag {

    /**
     * 主键 Id
     */
    @Id
    @CustomIdPolicy
    private Long id;

    /**
     * 父标签 Id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 标签路径
     */
    private String path;

    /**
     * 标签名称
     */
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    /**
     * 标签颜色
     */
    @Column(length = 7)
    private String color;
}