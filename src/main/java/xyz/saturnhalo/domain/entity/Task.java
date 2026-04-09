package xyz.saturnhalo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import xyz.saturnhalo.domain.entity.enums.TaskStatus;
import xyz.saturnhalo.domain.entity.enums.TaskType;
import xyz.saturnhalo.domain.entity.enums.TaskPriority;

import java.time.LocalDateTime;

/**
 * 任务 Entity
 */
@Data
@TableName(value = "tasks")
public class Task {

    /**
     * 主键 Id
     */
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务类型
     */
    @TableField(value = "type")
    private TaskType type;

    /**
     * 任务状态
     */
    @TableField(value = "status")
    private TaskStatus status;

    /**
     * 任务优先级
     */
    @TableField(value = "priority")
    private TaskPriority priority;

    /**
     * 上下文对象
     */
    @TableField(value = "context")
    private String context;

    /**
     * 任务执行结果
     */
    @TableField(value = "result_data")
    private String resultData;

    /**
     * 任务失败原因
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 日志追踪信息
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 标签树版本号
     */
    @Version
    @TableField(value = "version")
    private Long version;
}