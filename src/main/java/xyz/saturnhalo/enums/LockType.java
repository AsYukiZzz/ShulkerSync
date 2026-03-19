package xyz.saturnhalo.enums;

/**
 * 分布式锁类型枚举
 */
public enum LockType {

    /**
     * 可重入互斥锁
     */
    REENTRANT,

    /**
     * 写锁
     */
    WRITE,

    /**
     * 读锁
     */
    READ
}