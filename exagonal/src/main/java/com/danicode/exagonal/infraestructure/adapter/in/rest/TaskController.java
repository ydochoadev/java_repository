package com.danicode.exagonal.infraestructure.adapter.in.rest;

import com.danicode.exagonal.application.port.in.CreateTaskUseCase;
import com.danicode.exagonal.application.port.in.GetTaskUseCase;
import com.danicode.exagonal.application.port.in.ListTasksUseCase;
import com.danicode.exagonal.domain.model.Task;
import com.danicode.exagonal.infraestructure.adapter.in.rest.dto.CreateTaskRequest;
import com.danicode.exagonal.infraestructure.adapter.in.rest.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final GetTaskUseCase getTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        Task task = Task.builder().
                title(createTaskRequest.getTitle())
                .description(createTaskRequest.getDescription())
                .build();

        Task saved = this.createTaskUseCase.create(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAll() {
        List<TaskResponse> responseList = this.listTasksUseCase.listAll()
                .stream()
                .map(TaskResponse::from)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        Task task = this.getTaskUseCase.getById(id);
        return ResponseEntity.ok(TaskResponse.from(task));
    }
}
