package com.atlas.bank.atlas.application.port.out;

import com.atlas.bank.atlas.domain.model.customer.Customer;

import java.util.Optional;

public interface CustomerRepositoryPort {
    Optional<Customer> findById(Long id);

    Customer save(Customer customer);
}
