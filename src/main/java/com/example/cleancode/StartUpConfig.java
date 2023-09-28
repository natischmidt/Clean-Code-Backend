package com.example.cleancode;

import com.example.cleancode.availability.Availability;
import com.example.cleancode.availability.AvailabilityRepository;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Configuration
public class StartUpConfig {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final AvailabilityRepository availabilityRepository;
    private final JobRepository jobRepository;

    public StartUpConfig(EmployeeRepository employeeRepository, CustomerRepository customerRepository, AvailabilityRepository availabilityRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.availabilityRepository = availabilityRepository;
        this.jobRepository = jobRepository;
    }

    @Bean
    public CommandLineRunner initCustomerDatabase(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(new Customer(
                    "lars",
                    "olof",
                    "9105055555",
                    "hej@hej.hej",
                    "0730123123",
                    "adressgatan 12",
                    CustomerType.BUSINESS
            ));
            customerRepository.save(new Customer(
                    "Hilfrid",
                    "Ragnarsson",
                    "2504306666",
                      "hilfrid@supercompany.com",
                    "0730424258",
                    "Ragnargatan 25",
                    CustomerType.PRIVATE
            ));
        };
    }
    @Bean
    public CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository){
        return args -> {
            employeeRepository.save(new Employee(
                    "Kent",
                    "olofsson",
                    "password",
                    "5607144544",
                    "kent@kent.kent",
                    "0734123323",
                    "adressvägen 23",
                    Role.EMPLOYEE,
                    List.of()));

            employeeRepository.save(new Employee(
                    "Admin",
                    "Adminsson",
                    "password",
                    "5607144543",
                    "kentadmin@admin.kent",
                    "0742424242",
                    "adressvägen 65",
                    Role.ADMIN,
                    List.of()));
        };
    }
    @Bean
    public CommandLineRunner initJobDatabase(JobRepository jobRepository){
        return args -> {
            jobRepository.save(new Job(Jobtype.BASIC, "2023-11-24T12:00", JobStatus.PENDING,
                    55, PaymentOption.KLARNA, employeeRepository.findById(1L).get(),
                    customerRepository.findById(1L).get()));
        };
    }

    @Bean
    public CommandLineRunner initAvailabilityDB(AvailabilityRepository availabilityRepository){
        return args -> {
            Availability availability = new Availability();
            availability.setStartTime(LocalDateTime.of(2023, Month.NOVEMBER, 24, 9, 0));
            availability.setEndTime(LocalDateTime.of(2023, Month.NOVEMBER, 24, 17, 0));

            Employee employee1 = employeeRepository.findById(1L).get();
            Employee employee2 = employeeRepository.findById(2L).get();
            availability.setEmployees(List.of(employee1, employee2));

            Job job1 = jobRepository.findById(1L).get();
            availability.setJobs(List.of(job1));

            availabilityRepository.save(availability);
        };
    }
}
