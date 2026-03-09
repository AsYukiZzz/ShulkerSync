package xyz.saturnhalo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.saturnhalo.anno.CustomIdPolicy;
import xyz.saturnhalo.enums.TaskStatus;
import xyz.saturnhalo.enums.TaskType;

import java.time.LocalDateTime;

/**
 * 任务 Entity
 */
@Data
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * 主键 Id
     */
    @Id
    @CustomIdPolicy
    private Long id;

    /**
     * 任务类型
     */
    private TaskType type;

    /**
     * 任务状态
     */
    private TaskStatus status;

    /**
     * 任务结果
     */
    private String result;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}