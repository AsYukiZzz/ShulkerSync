package xyz.saturnhalo.enums;

import lombok.AllArgsConstructor;

/**
 * 任务状态枚举
 */
@AllArgsConstructor
public enum TaskStatus implements BaseEnum{

    PENDING(0, "未执行"),
    SUCCESS(1, "执行成功"),
    FAILURE(2, "执行失败");

    private final Integer code;
    private final String displayName;

    @Override
    public Integer getCode() {
        return code;
    }
}