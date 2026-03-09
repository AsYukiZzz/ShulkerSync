package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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