package xyz.saturnhalo.anno;

import org.hibernate.annotations.IdGeneratorType;
import xyz.saturnhalo.generator.IdGenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * 自定义 Id 策略
 * 使用该注解标注的字段，会使用对应的 Id 生成策略生成 Id
 */
@IdGeneratorType(IdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, METHOD})
public @interface CustomIdPolicy {
}