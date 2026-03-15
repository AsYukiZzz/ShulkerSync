package xyz.saturnhalo.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公有字段填充配置
 */
@Slf4j
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    /**
     * 插入时填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("公共字段填充-插入，实体类: {}", metaObject.getOriginalObject().getClass().getSimpleName());
        // 创建时间
        this.setFieldValByName("createAt", LocalDateTime.now(), metaObject);
        // 更新时间
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
    }

    /**
     * 更新时填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("公共字段填充-更新，实体类: {}", metaObject.getOriginalObject().getClass().getSimpleName());
        // 更新时间
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
    }
}