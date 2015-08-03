package com.github.lambda.spring.data.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByName(String name);
    List<Customer> findByAddress(String address);
}
