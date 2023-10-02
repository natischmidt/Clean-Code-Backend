package com.example.cleancode;

import com.example.cleancode.availability.Availability;
import com.example.cleancode.availability.AvailabilityRepository;
import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.employees.Salary;
import com.example.cleancode.enums.*;
import com.example.cleancode.job.Job;
import com.example.cleancode.job.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class StartUpConfig {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public StartUpConfig(EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Bean
    public CommandLineRunner initCustomerDatabase(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(
                    "lars",
                    "olof",
                    "Larssas måleri AB",
                    "123445660654-orgnr",
                    "hej@hej.hej",
                    "0730123123",
                    "adressgatan 12",
                    CustomerType.BUSINESS
            ));
            customerRepository.save(new Customer(
                    "Hilfrid",
                    "Ragnarsson",
                    "hilfrid@supercompany.com",
                    "0730424258",
                    "Ragnargatan 25",
                    CustomerType.PRIVATE
            ));
        };
    }

    @Bean
    public CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository) {
        Salary salary1 = new Salary(100);
        Employee employee1 = new Employee(
                "Kent",
                "olofsson",
                "password",
                "5607144544",
                "kent@kent.kent",
                "0734123323",
                "adressvägen 23",
                Role.EMPLOYEE,
                salary1,
                List.of());

        Salary salary2 = new Salary(150);
        Employee employee2 = new Employee(
                "Admin",
                "Adminsson",
                "password",
                "5607144543",
                "kentadmin@admin.kent",
                "0742424242",
                "adressvägen 65",
                Role.ADMIN,
                salary2,
                List.of());

        salary1.setEmployee(employee1);
        salary2.setEmployee(employee2);

        return args -> {
            employeeRepository.save(employee1);

            employeeRepository.save(employee2);
        };
    }

    @Bean
    public CommandLineRunner initJobDatabase(JobRepository jobRepository) {
        return args -> {
            LocalDateTime date = LocalDateTime.parse("2023-11-24T12:00");
            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.EVENING,
                    JobStatus.PENDING,
                    55,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(1L).get(),
                    customerRepository.findById(1L).get()));

            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.NONE,
                    JobStatus.PENDING,
                    50,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(1L).get(),
                    customerRepository.findById(2L).get()));

            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.AFTER_NONE,
                    JobStatus.PENDING,
                    55,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(2L).get(),
                    customerRepository.findById(1L).get()));
        };
    }


}
