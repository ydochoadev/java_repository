package com.danicode.exagonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public void complete() {
        if (this.status == TaskStatus.COMPLETED) throw new IllegalStateException("Task is already completed");

        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void reopen() {
        if (this.status == TaskStatus.PENDING) throw new IllegalStateException("Task is pending");

        this.status = TaskStatus.PENDING;
        this.completedAt = null;
    }

    public void initDefaults() {
        if (this.status == null) this.status = TaskStatus.PENDING;
        if (createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
