package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签节点 Entity
 */
@Data
@TableName(value = "tag_node")
public class TagNode {

    /**
     * 主键 Id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 所属标签树 Id
     */
    @TableField(value = "tree_id")
    private Long treeId;

    /**
     * 父标签节点 Id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 标签路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 标签名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 标签颜色 (HEX 格式，如 #FF5733)
     */
    @TableField(value = "color")
    private String color;

    /**
     * 同级排序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

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
     * 标签树版本号
     */
    @Version
    @TableField(value = "version")
    private Long version;
}