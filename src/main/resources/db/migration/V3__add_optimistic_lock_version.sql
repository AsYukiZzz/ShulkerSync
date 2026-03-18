-- ==============================================================================
-- Add_Optimistic_Lock_Version.sql
-- ==============================================================================

-- 更改说明
-- 1) 更改乐观锁的实现字段：由原来的 update_at 变更为 version

ALTER TABLE tag_tree ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE tag_node ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE modpacks ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE versions ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE assets ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE tasks ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';
ALTER TABLE settings ADD COLUMN version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号';