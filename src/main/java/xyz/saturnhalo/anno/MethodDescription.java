package xyz.saturnhalo.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务日志注解
 * 用于在 WebLogAspect 切面类中打印 Controller 层方法作用描述
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodDescription {

    /**
     * 作用描述
     */
    String value() default "";
}