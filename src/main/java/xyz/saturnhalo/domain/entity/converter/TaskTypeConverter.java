package xyz.saturnhalo.domain.entity.converter;

import jakarta.persistence.Converter;
import xyz.saturnhalo.enums.TaskType;

/**
 * TaskType 枚举转换器
 */
@Converter(autoApply = true)
public class TaskTypeConverter extends AbstractEnumConverter<TaskType> {
    public TaskTypeConverter() {
        super(TaskType.class);
    }
}