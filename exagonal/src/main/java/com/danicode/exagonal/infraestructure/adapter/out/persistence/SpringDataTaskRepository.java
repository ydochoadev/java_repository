package com.danicode.exagonal.infraestructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {
}
