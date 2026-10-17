package com.danicode.exagonal.infraestructure.adapter.out.persistence;

import com.danicode.exagonal.domain.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {
    public Task toDomain(TaskJpaEntity entity) {
        if (entity == null) return null;
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

    public TaskJpaEntity toJpaEntity(Task task) {
        if (task == null) return null;
        return TaskJpaEntity.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }
}
