package com.danicode.exagonal.application.service;

import com.danicode.exagonal.application.port.in.CreateTaskUseCase;
import com.danicode.exagonal.application.port.out.TaskRepositoryPort;
import com.danicode.exagonal.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Task execute(Task task) {
        return this.taskRepositoryPort.save(task);
    }
}
