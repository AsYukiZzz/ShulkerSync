package xyz.saturnhalo.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import xyz.saturnhalo.utils.IdUtils;

/**
 * 自定义 Hibernate ID 生成策略
 */
public class IdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        // 返回自定义 ID
        return IdUtils.nextId();
    }
}