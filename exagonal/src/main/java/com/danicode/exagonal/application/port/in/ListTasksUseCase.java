package com.danicode.exagonal.application.port.in;

import com.danicode.exagonal.domain.model.Task;

import java.util.List;

public interface ListTasksUseCase {

    List<Task> listAll();
}
