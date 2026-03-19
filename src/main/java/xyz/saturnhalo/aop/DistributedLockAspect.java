package xyz.saturnhalo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import xyz.saturnhalo.anno.DistributedLock;
import xyz.saturnhalo.enums.LockType;
import xyz.saturnhalo.exception.BusinessException;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁与事务统一使用切面类
 */
@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        // 1. 解析 SpEL 表达式获取 Redis Key
        String lockKey = parseKey(joinPoint, distributedLock.key());

        // 2. 根据 LockType 获取对应分布式锁
        RLock lock = getLockInstance(lockKey, distributedLock.type());

        boolean isLocked = false;
        try {
            // 3. 尝试加锁
            isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS);
            if (!isLocked) {
                log.warn("获取分布式锁(type={},key={})超时", distributedLock.type(), lockKey);
                throw new BusinessException("系统繁忙或有其他人正在操作，请稍后再试！");
            }
            log.debug("获取分布式锁(type={},key={})成功", distributedLock.type(), lockKey);

            // 4. 判断是否需要事务包裹
            if (distributedLock.withTransaction()) {
                // 4.1 使用事务包裹
                return transactionTemplate.execute(status -> {
                    try {
                        // 4.1.1 执行原方法
                        return joinPoint.proceed();
                    } catch (RuntimeException | Error e) {
                        status.setRollbackOnly();
                        throw e;
                    } catch (Throwable e) {
                        status.setRollbackOnly();
                        log.error("分布式锁事务内发生未知异常", e);
                        throw new RuntimeException("系统内部异常", e);
                    }
                });
            } else {
                // 4.2 不使用事务包裹
                return joinPoint.proceed();
            }
        } finally {
            // 5. 释放分布式锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("成功释放分布式锁: [{}]", lockKey);
            }
        }
    }

    /**
     * 获取分布式锁对象
     */
    private RLock getLockInstance(String lockKey, LockType lockType) {
        return switch (lockType) {
            case READ -> redissonClient.getReadWriteLock(lockKey).readLock();
            case WRITE -> redissonClient.getReadWriteLock(lockKey).writeLock();
            default -> redissonClient.getLock(lockKey);
        };
    }

    /**
     * 解析分布式锁的 Key
     */
    private String parseKey(ProceedingJoinPoint joinPoint, String spelString) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] arguments = joinPoint.getArgs();
        String[] parameterNames = discoverer.getParameterNames(method);

        // 1. 不存在方法参数：跳过解析。删除单引号并直接返回
        if (parameterNames == null || parameterNames.length == 0) {
            return spelString.replace("'", "");
        }

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }

        try {
            // 2. 解析 SpEL
            return parser.parseExpression(spelString).getValue(context, String.class);
        } catch (Exception e) {
            log.error("解析分布式锁 Key 失败. 表达式: {}", spelString, e);
            throw new RuntimeException("系统异常：并发控制参数解析失败");
        }
    }
}