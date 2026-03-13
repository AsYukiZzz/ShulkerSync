-- ==========================================
-- ShulkerSync Database Initialization Script
-- ==========================================

-- 说明
-- 1) 创建时间、更新时间字段由业务层赋值，不交由数据库管理
-- 2) 不使用物理外键，改由使用逻辑外键，并在业务层维护关联关系
-- 3) 状态字段一般使用枚举，在存入数据库时自动转换为对应整型值，取出再转换为枚举对象，具体定义请看枚举类

-- 语句
-- 1. 创建标签表
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT PRIMARY KEY,
    parent_id BIGINT,                               -- 父标签 ID（若当前标签已是顶级标签，则赋值为 -1）
    path VARCHAR(255) NOT NULL,                     -- 标签路径，如"/1/2/3"，数字代指 ID
    name VARCHAR(50) NOT NULL,                      -- 标签名称
    color VARCHAR(7),                               -- 标签颜色，采用 HEX 格式，如 #FF5733
    created_at DATETIME NOT NULL,                   -- 创建时间
    updated_at DATETIME NOT NULL                    -- 更新时间
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tags_path ON tags(path);
CREATE INDEX idx_tags_parent_id ON tags(parent_id);
CREATE UNIQUE INDEX uk_tags_parent_name ON tags(parent_id, name);       -- 父标签 ID 与标签名字的联合唯一索引，确保统一层级下标签的唯一性


-- 2. 创建整合包表
CREATE TABLE IF NOT EXISTS modpacks (
    id BIGINT PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,              -- 路由标识，全局唯一
    name VARCHAR(100) NOT NULL,                     -- 整合包名称，允许重复
    description TEXT,                               -- 整合包描述
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 3. 创建整合包与标签关联表 (多对多)
CREATE TABLE IF NOT EXISTS modpack_tag (
    modpack_id BIGINT NOT NULL,                     -- 逻辑外键：指向 modpacks_id
    tag_id BIGINT NOT NULL,                         -- 逻辑外键：指向 tags_id
    PRIMARY KEY (modpack_id, tag_id)                -- 复合主键，同时作为 modpack_id 的前缀索引
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_modpack_tag_tag_id ON modpack_tag(tag_id);


-- 4. 创建版本表
CREATE TABLE IF NOT EXISTS versions (
    id BIGINT PRIMARY KEY,
    modpack_id BIGINT NOT NULL,                     -- 逻辑外键：指向 modpacks_id
    branch VARCHAR(50) NOT NULL,                    -- 版本分支，如 main、dev、beta
    modpack_version VARCHAR(30) NOT NULL,           -- 整合包版本号
    minecraft_version VARCHAR(30) NOT NULL,         -- 游戏版本号
    manifest_json JSON NOT NULL,                    -- 版本清单文件
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE (modpack_id, branch, modpack_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_versions_modpack_branch ON versions(modpack_id, branch);


-- 5. 创建文件资产表
CREATE TABLE IF NOT EXISTS assets (
    hash VARCHAR(64) PRIMARY KEY,                   -- 文件 hash 值
    size BIGINT NOT NULL,                           -- 文件大小（单位：字节）
    storage_type INTEGER NOT NULL,                  -- 存储策略（枚举）
    storage_path VARCHAR(500) NOT NULL,             -- 云端存储 Key 或本地路径
    reference_count INTEGER NOT NULL DEFAULT 0,     -- 引用计数器（用于清理无效的文件）
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_assets_reference_count ON assets(reference_count);


-- 6. 创建异步任务表
CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(36) PRIMARY KEY,
    type INTEGER NOT NULL,                          -- 异步任务类型（枚举）
    status INTEGER NOT NULL,                        -- 异步任务状态（枚举）
    progress INTEGER NOT NULL DEFAULT 0,            -- 任务进度
    result JSON,                                    -- 执行结果
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tasks_status ON tasks(status);


-- 7. 创建系统配置表
CREATE TABLE IF NOT EXISTS settings (
    id BIGINT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,        -- 配置键名
    config_value TEXT,                              -- 配置值
    description VARCHAR(255),                       -- 具体说明
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;