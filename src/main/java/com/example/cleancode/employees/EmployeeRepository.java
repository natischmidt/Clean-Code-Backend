package com.example.cleancode.employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findBySsNumber(String ssNumber);



}
