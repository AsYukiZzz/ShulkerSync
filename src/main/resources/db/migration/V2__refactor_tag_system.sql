-- ==============================================================================
-- Refactor Tag System Script
-- ==============================================================================

-- 更改说明
-- 1) 重构标签系统，引入 tag_tree 与 tag_node 双表架构，支持整树操作与乐观锁

-- 步骤说明
-- 1) 删除标签表、标签整合包关系表
-- 2) 创建 tag_tree、tag_node 和 modpack_tag_node 表

-- 1. 删除旧表
DROP TABLE IF EXISTS modpack_tag;
DROP TABLE IF EXISTS tags;

-- 2. 创建 tag_tree 表 (定义全局树属性与并发控制)
CREATE TABLE tag_tree (
    id BIGINT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,               -- 标签树名称
    description VARCHAR(255),                       -- 标签树描述
    version BIGINT NOT NULL DEFAULT 0,              -- 标签树版本号（乐观锁机制）
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. 创建 tag_node 表 (记录具体标签信息及拓扑链路)
CREATE TABLE tag_node (
    id BIGINT PRIMARY KEY,
    tree_id BIGINT NOT NULL,                        -- 所属标签树 ID
    parent_id BIGINT NOT NULL,                      -- 父节点 ID（顶级标签则为 -1）
    path VARCHAR(255) NOT NULL,                     -- 节点链路 (如 "/{tree_id}/1/2/3/")
    name VARCHAR(50) NOT NULL,                      -- 标签名称
    color VARCHAR(7),                               -- 标签颜色 (HEX 格式)
    sort_order INTEGER NOT NULL DEFAULT 0,          -- 同级排序号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_tree_parent_name (tree_id, parent_id, name) -- 联合唯一：同树同层级下名称不重复
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 建立查询索引
CREATE INDEX idx_tag_node_tree_id ON tag_node(tree_id);
CREATE INDEX idx_tag_node_path ON tag_node(path);

-- 4. 创建 modpack_tag_node 表
CREATE TABLE modpack_tag_node (
    modpack_id BIGINT NOT NULL,                     -- 逻辑外键指向 modpacks.id
    node_id BIGINT NOT NULL,                        -- 逻辑外键指向 tag_node.id
    PRIMARY KEY (modpack_id, node_id)               -- 复合主键
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 建立反向查询索引 (通过标签找整合包)
CREATE INDEX idx_modpack_tag_node_id ON modpack_tag_node(node_id);