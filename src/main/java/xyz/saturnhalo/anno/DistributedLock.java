package xyz.saturnhalo.anno;

import xyz.saturnhalo.enums.LockType;

import java.lang.annotation.*;

/**
 * 分布式锁与事务一体化注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的 Key（支持 SpEL）
     */
    String key();

    /**
     * 是否开启事务
     */
    boolean withTransaction() default true;

    /**
     * 获取锁的类型
     */
    LockType type() default LockType.REENTRANT;

    /**
     * 获取锁的最长等待时间
     */
    long waitTime() default 3;

    /**
     * 持有锁的超时释放时间
     */
    long leaseTime() default 10;
}