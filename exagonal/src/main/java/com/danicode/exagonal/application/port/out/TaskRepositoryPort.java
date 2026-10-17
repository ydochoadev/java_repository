package com.danicode.exagonal.application.port.out;

import com.danicode.exagonal.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    Task save(Task task);

    List<Task> findAll();

    Optional<Task> findById(Long id);
}
