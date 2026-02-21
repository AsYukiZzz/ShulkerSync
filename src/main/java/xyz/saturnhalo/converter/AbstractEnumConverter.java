package xyz.saturnhalo.converter;

import jakarta.persistence.AttributeConverter;
import xyz.saturnhalo.enums.BaseEnum;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 转换器父类
 */
public abstract class AbstractEnumConverter<E extends Enum<E> & BaseEnum> implements AttributeConverter<E, Integer> {

    private final ConcurrentHashMap<Integer, E> enumCache = new ConcurrentHashMap<>();

    public AbstractEnumConverter(Class<E> enumClass) {
        // 建立枚举缓存（code:enum）
        E[] enums = enumClass.getEnumConstants();
        for (E e : enums) {
            enumCache.put(e.getCode(), e);
        }
    }

    /**
     * 实体对象 -> 数据库
     */
    @Override
    public Integer convertToDatabaseColumn(E e) {
        return e == null ? null : e.getCode();
    }

    /**
     * 数据库 -> 实体对象
     */
    @Override
    public E convertToEntityAttribute(Integer value) {
        return value == null ? null : enumCache.get(value);
    }
}