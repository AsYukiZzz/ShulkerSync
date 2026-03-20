package xyz.saturnhalo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import xyz.saturnhalo.domain.entity.enums.BaseEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举自动映射
 * 适用于参数接收阶段（将接收到的参数转换成对应的枚举对象并赋值给实体类中对应的属性）
 */
@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    /**
     * Converter 缓存列表
     */
    private final Map<Class<? extends BaseEnum>, Converter<String, ? extends BaseEnum>> CONVERTER_CACHE = new ConcurrentHashMap<>();

    /**
     * Converter 获取方法
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEnum> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return (Converter<String, T>) CONVERTER_CACHE.computeIfAbsent(targetType, StringToEnumConverter::new);
    }

    /**
     * Converter 内部类
     */
    private static class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {

        // Enum 对象缓存
        private final Map<String, T> enumMap = new HashMap<>();

        public StringToEnumConverter(Class<T> targetType) {

            // 1. 获取当前枚举类型的全部对象
            T[] enums = targetType.getEnumConstants();

            // 2. 依照 Code:Enum(枚举对象) 的方式存入缓存
            for (T t : enums) {
                Object code = t.getCode();
                if (code != null) {
                    enumMap.put(code.toString(), t);
                }
            }
        }

        /**
         * 根据传入的 String Code 转换成对应的 Enum 对象
         */
        @Nullable
        @Override
        public T convert(@NonNull String source) {
            // 1. 健壮性判断
            if (source.isEmpty()) {
                return null;
            }

            // 2. 通过 GetCode 方法获取 Enum 对象
            String codeValue = source.trim();
            T t = enumMap.get(codeValue);
            if (t == null) {
                throw new IllegalArgumentException(
                        String.format("枚举 %s 中不存在 code=[%s] 的值",
                                enumMap.values().iterator().next().getClass().getSimpleName(), source)
                );
            }
            return t;
        }
    }
}