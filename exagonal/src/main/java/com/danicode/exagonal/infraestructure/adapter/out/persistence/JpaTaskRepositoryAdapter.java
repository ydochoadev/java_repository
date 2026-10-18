package com.danicode.exagonal.infraestructure.adapter.out.persistence;

import com.danicode.exagonal.application.port.out.TaskRepositoryPort;
import com.danicode.exagonal.domain.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTaskRepositoryAdapter implements TaskRepositoryPort {

    private final SpringDataTaskRepository springDataTaskRepository;
    private final TaskPersistenceMapper mapper;

    @Override
    public Task save(Task task) {
        task.initDefaults();
        TaskJpaEntity entity = this.mapper.toJpaEntity(task);
        TaskJpaEntity saved = this.springDataTaskRepository.save(entity);

        return this.mapper.toDomain(saved);
    }

    @Override
    public List<Task> findAll() {
        return this.springDataTaskRepository.findAll()
                .stream()
                .map(this.mapper::toDomain).toList();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return this.springDataTaskRepository.findById(id).map(this.mapper::toDomain);
    }

    @Override
    public Task update(Task task) {
        TaskJpaEntity entity = this.mapper.toJpaEntity(task);
        TaskJpaEntity saved = this.springDataTaskRepository.save(entity);

        return this.mapper.toDomain(saved);
    }
}
