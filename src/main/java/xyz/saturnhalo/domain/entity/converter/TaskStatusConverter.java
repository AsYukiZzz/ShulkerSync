package xyz.saturnhalo.domain.entity.converter;

import jakarta.persistence.Converter;
import xyz.saturnhalo.enums.TaskStatus;

/**
 * TaskStatus 枚举转换器
 */
@Converter(autoApply = true)
public class TaskStatusConverter extends AbstractEnumConverter<TaskStatus> {
    public TaskStatusConverter() {
        super(TaskStatus.class);
    }
}