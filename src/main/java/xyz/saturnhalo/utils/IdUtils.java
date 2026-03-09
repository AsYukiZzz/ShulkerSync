package xyz.saturnhalo.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Id 生成工具类
 */
@Slf4j
public class IdUtils {

    // 起始时间
    private static final long EPOCH = 1772985600000L;

    // 序列号位数
    private static final long SEQUENCE_BITS = 22L;

    // 序列号极值
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    // 上次请求时间粗
    private static long lastLogicalTime = -1L;

    // 请求序列号
    private static long sequence = 0L;

    // 容错序列号

    /**
     * Id 生成器
     */
    public static synchronized Long nextId() {
        // 1. 使用逻辑时间戳（选择较大的时间作为当前时间戳，避免时钟回拨问题）
        long curPhysicalTime = Math.max(lastLogicalTime, System.currentTimeMillis());

        // 2. 判断是否为同毫秒请求（两次请求的时间戳相同）
        if (curPhysicalTime == lastLogicalTime) {
            // 3.1 是同毫秒请求：序列号递增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 3.1.1 请求序列号溢出归 0：预先占用下一毫秒的序列
                lastLogicalTime++;
            }
        } else {
            // 3.2 非同毫秒请求：序列号清 0
            sequence = 0L;
            lastLogicalTime = curPhysicalTime;
        }

        // 3. 生成 Id 并返回
        return ((lastLogicalTime - EPOCH) << SEQUENCE_BITS) | sequence;
    }
}