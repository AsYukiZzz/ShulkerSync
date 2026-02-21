package xyz.saturnhalo.converter;

import jakarta.persistence.Converter;
import xyz.saturnhalo.enums.TaskType;

/**
 * TaskType 枚举转换器
 */
@Converter(autoApply = true)
class TaskTypeConverter extends AbstractEnumConverter<TaskType> {
    public TaskTypeConverter() {
        super(TaskType.class);
    }
}