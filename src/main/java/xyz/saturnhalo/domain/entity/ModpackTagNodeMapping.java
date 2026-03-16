package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 整合包与标签节点关联 Entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "modpack_tag_node")
public class ModpackTagNodeMapping {

    /**
     * 整合包 Id
     */
    @TableField(value = "modpack_id")
    private Long modpackId;

    /**
     * 标签节点 Id
     */
    @TableField(value = "node_id")
    private Long nodeId;
}