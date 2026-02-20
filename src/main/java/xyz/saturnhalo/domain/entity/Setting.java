package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 系统配置项 Entity
 */
@Data
@Entity
@Table(name = "settings")
public class Setting {

    /**
     * 主键 Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配置项键
     */
    @Column(name = "config_key", length = 100)
    private String key;

    /**
     * 配置项值
     */
    @Column(name = "config_value", columnDefinition = "Text")
    private String value;

    /**
     * 配置项描述
     */
    private String description;
}