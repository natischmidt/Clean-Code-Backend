package com.example.cleancode.employees;

import com.example.cleancode.enums.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findBySsNumber(String ssNumber);

    @Query("SELECT e FROM Employee e JOIN e.availabilities a WHERE a.date = :date AND a.timeSlots = :timeSlots")
    List<Employee> findAvailableEmployees(@Param("date") LocalDateTime date, @Param("timeSlots") TimeSlots timeSlots);


}
