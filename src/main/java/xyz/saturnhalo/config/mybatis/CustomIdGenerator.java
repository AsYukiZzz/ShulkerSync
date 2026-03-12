package xyz.saturnhalo.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;
import xyz.saturnhalo.utils.IdUtils;

/**
 * MyBatisPlus 自定义 ID 生成
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    /**
     * ID 生成
     */
    @Override
    public Long nextId(Object entity) {
        return IdUtils.nextId();
    }
}