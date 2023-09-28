package com.example.cleancode.availability;

import com.example.cleancode.employees.Employee;
import com.example.cleancode.job.Job;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToMany
    private List<Employee> employees;

    @ManyToMany
    private List<Job> jobs;
}
