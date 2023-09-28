package com.example.cleancode.employees;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findBySsNumber(String ssNumber);

    @Query("SELECT e FROM Employee e WHERE e.id NOT IN (SELECT ae.id FROM Availability a JOIN a.employees ae WHERE a.date = ?1 AND a.timeSlots = ?2)")
    List<Employee> findUnbookedEmployees(LocalDateTime date, TimeSlots timeSlot);



}
