package com.danicode.exagonal.application.port.out;

import com.danicode.exagonal.domain.model.Task;

public interface TaskRepositoryPort {
    Task save(Task task);
    // Buscar, listar, etc
}
