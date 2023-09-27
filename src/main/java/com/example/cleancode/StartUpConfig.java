package com.example.cleancode;

import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.enums.*;
import com.example.cleancode.job.Job;
import com.example.cleancode.job.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StartUpConfig {

    private final EmployeeRepository employeeRepository;

    public StartUpConfig(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public CommandLineRunner initCustomerDatabase(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(new Customer("lars", "olof", "9105055555",
                    "hej@hej.hej", "0730123123",
                    "adressgatan 12", CustomerType.BUSINESS ));
        };
    }
    @Bean
    public CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository){
        return args -> {
            employeeRepository.save(new Employee("Kent","olofsson",
                    "5607144543", "kent@kent.kent", 0734123322, "adressvägen 23",
                    Role.EMPLOYEE, List.of()));
            employeeRepository.save(new Employee("Admin","Adminsson",
                    "5607144543", "kent@kent.kent", 0742424242, "adressvägen 65",
                    Role.ADMIN, List.of()));
        };
    }
    @Bean
    public CommandLineRunner initJobDatabase(JobRepository jobRepository){
        return args -> {
            jobRepository.save(new Job(Jobtype.BASIC, null, JobStatus.PENDING,
                    55, PaymentOption.KLARNA, employeeRepository.findById(1L).get()));
        };
    }
}
