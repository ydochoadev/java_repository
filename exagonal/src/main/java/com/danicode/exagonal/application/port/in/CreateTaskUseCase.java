package com.danicode.exagonal.application.port.in;

import com.danicode.exagonal.domain.model.Task;

public interface CreateTaskUseCase {
    Task create(Task task);
}
