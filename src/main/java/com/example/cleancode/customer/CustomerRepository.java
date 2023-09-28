package com.example.cleancode.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long > {
        Optional<Customer> findBySsNumber(String ssNumber);
        Optional<Customer> findByEmail(String email);
}
