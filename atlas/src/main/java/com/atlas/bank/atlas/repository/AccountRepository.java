package com.atlas.bank.atlas.repository;

import com.atlas.bank.atlas.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
