package com.danicode.exagonal.infraestructure.adapter.in.rest;

import com.danicode.exagonal.application.port.in.CreateTaskUseCase;
import com.danicode.exagonal.domain.model.Task;
import com.danicode.exagonal.infraestructure.adapter.in.rest.dto.CreateTaskRequest;
import com.danicode.exagonal.infraestructure.adapter.in.rest.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        Task task = Task.builder().
                title(createTaskRequest.getTitle())
                .description(createTaskRequest.getDescription())
                .build();

        Task saved = this.createTaskUseCase.execute(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(saved));
    }
}
