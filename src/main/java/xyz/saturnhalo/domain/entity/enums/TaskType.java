package xyz.saturnhalo.domain.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

/**
 * 任务类型枚举
 */
@AllArgsConstructor
public enum TaskType implements BaseEnum{

    PROCESS_MODPACK(
            10,
            "整合包处理",
            "计算文件的SHA-256哈希，去重存储并生成当前版本清单"
    );

    @EnumValue
    private final Integer code;
    private final String displayName;
    private final String description;

    @Override
    public Integer getCode() {
        return code;
    }
}