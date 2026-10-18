package com.danicode.exagonal.application.service;

import com.danicode.exagonal.application.port.in.*;
import com.danicode.exagonal.application.port.out.TaskRepositoryPort;
import com.danicode.exagonal.domain.exeption.TaskNotFoundException;
import com.danicode.exagonal.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService implements
        CreateTaskUseCase,
        ListTasksUseCase,
        GetTaskUseCase,
        CompleteTaskUseCase,
        ReopenTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Task create(Task task) {
        return this.taskRepositoryPort.save(task);
    }

    @Override
    public Task getById(Long id) {
        return this.taskRepositoryPort.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
    }

    @Override
    public List<Task> listAll() {
        return this.taskRepositoryPort.findAll();
    }

    @Override
    public Task complete(Long id) {
        Task task = this.taskRepositoryPort.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id));
        task.complete();

        return this.taskRepositoryPort.update(task);
    }

    @Override
    public Task reopen(Long id) {
        Task task = this.taskRepositoryPort.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id));
        task.reopen();

        return this.taskRepositoryPort.update(task);
    }
}
