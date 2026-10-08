package com.atlas.bank.atlas.infraestructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAccountrepository extends JpaRepository<AccountJpaEntity, Long> {
}
