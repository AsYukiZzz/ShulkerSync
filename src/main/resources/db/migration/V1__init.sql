-- ==========================================
-- ShulkerSync Database Initialization Script
-- ==========================================

-- 说明
-- 1) 创建时间、更新时间字段由业务层赋值，不交由数据库管理
-- 2) 不使用物理外键，改由使用逻辑外键，并在业务层维护关联关系
-- 3) 状态字段一般使用枚举，在存入数据库时自动转换为对应整型值，取出再转换为枚举对象，具体定义请看枚举类
-- 4) 采用双表架构(tag_tree/tag_node)管理标签系统，支持整树操作与乐观锁
-- 5) 引入 version 字段用于统一实现乐观锁控制

-- 语句
-- 1. 创建标签树表 (定义全局树属性与并发控制)
CREATE TABLE IF NOT EXISTS tag_tree (
    id BIGINT PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,              -- 标签树版本号（乐观锁机制）
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2. 创建标签节点表 (记录具体标签信息及拓扑链路)
CREATE TABLE IF NOT EXISTS tag_node (
    id BIGINT PRIMARY KEY,
    tree_id BIGINT NOT NULL,                        -- 所属标签树 ID
    parent_id BIGINT NOT NULL,                      -- 父节点 ID（顶级标签则为 -1）
    path VARCHAR(255) NOT NULL,                     -- 节点链路 (如 "/{tree_id}/1/2/3/")
    name VARCHAR(50) NOT NULL,                      -- 标签名称
    color VARCHAR(7),                               -- 标签颜色 (HEX 格式)
    sort_order INTEGER NOT NULL DEFAULT 0,          -- 同级排序号
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_tree_parent_name (tree_id, parent_id, name) -- 联合唯一：同树同层级下名称不重复
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tag_node_tree_id ON tag_node(tree_id);
CREATE INDEX idx_tag_node_path ON tag_node(path);


-- 3. 创建整合包表
CREATE TABLE IF NOT EXISTS modpacks (
    id BIGINT PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,              -- 路由标识，全局唯一
    name VARCHAR(100) NOT NULL,                     -- 整合包名称，允许重复
    description TEXT,                               -- 整合包描述
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 4. 创建整合包与标签节点关联表 (多对多)
CREATE TABLE IF NOT EXISTS modpack_tag_node (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 逻辑主键，兼容 MyBatis-Plus 的 ById 及其相关增强操作
    modpack_id BIGINT NOT NULL,                     -- 逻辑外键指向 modpacks.id
    node_id BIGINT NOT NULL,                        -- 逻辑外键指向 tag_node.id
    UNIQUE KEY uk_modpack_node (modpack_id, node_id) -- 复合唯一约束：确保同一个整合包不会重复绑定同一个标签
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_modpack_tag_node_id ON modpack_tag_node(node_id);


-- 5. 创建版本表
CREATE TABLE IF NOT EXISTS versions (
    id BIGINT PRIMARY KEY,
    modpack_id BIGINT NOT NULL,                     -- 逻辑外键：指向 modpacks.id
    branch VARCHAR(50) NOT NULL,                    -- 版本分支，如 main、dev、beta
    modpack_version VARCHAR(30) NOT NULL,           -- 整合包版本号
    minecraft_version VARCHAR(30) NOT NULL,         -- 游戏版本号
    manifest_json JSON NOT NULL,                    -- 版本清单文件
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE (modpack_id, branch, modpack_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_versions_modpack_branch ON versions(modpack_id, branch);


-- 6. 创建文件资产表
CREATE TABLE IF NOT EXISTS assets (
    hash VARCHAR(64) PRIMARY KEY,                   -- 文件 hash 值
    size BIGINT NOT NULL,                           -- 文件大小（单位：字节）
    storage_type INTEGER NOT NULL,                  -- 存储策略（枚举）
    storage_path VARCHAR(500) NOT NULL,             -- 云端存储 Key 或本地路径
    reference_count INTEGER NOT NULL DEFAULT 0,     -- 引用计数器（用于清理无效的文件）
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_assets_reference_count ON assets(reference_count);


-- 7. 创建异步任务表
CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(36) PRIMARY KEY,
    type INTEGER NOT NULL,                          -- 异步任务类型（枚举）
    status INTEGER NOT NULL,                        -- 异步任务状态（枚举）
    progress INTEGER NOT NULL DEFAULT 0,            -- 任务进度
    result JSON,                                    -- 执行结果
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tasks_status ON tasks(status);


-- 8. 创建系统配置表
CREATE TABLE IF NOT EXISTS settings (
    id BIGINT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,        -- 配置键名
    config_value TEXT,                              -- 配置值
    description VARCHAR(255),                       -- 具体说明
    version BIGINT NOT NULL DEFAULT 0,              -- 乐观锁版本号
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;