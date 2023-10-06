package com.example.cleancode;

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
        salary1.setEmployee(employee1);


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
        salary2.setEmployee(employee2);


        Salary salary3 = new Salary(150);
        Employee employee3 = new Employee(
                "Cleany",
                "McCleanFace",
                "password",
                "5707144543",
                "mrclean@clean.se",
                "0742424542",
                "adressvägen 66",
                Role.EMPLOYEE,
                salary3,
                List.of());
        salary3.setEmployee(employee3);


        Salary salary4 = new Salary(150);
        Employee employee4 = new Employee(
                "Sten",
                "Sture",
                "password",
                "5607844543",
                "stenis@sten.kent",
                "07424212242",
                "adressvägen 67",
                Role.EMPLOYEE,
                salary4,
                List.of());
        salary4.setEmployee(employee4);

        Salary salary5 = new Salary(150);
        Employee employee5 = new Employee(
                "Namn",
                "Namnsson",
                "password",
                "5607142243",
                "namn@namn.namn",
                "0742423342",
                "adressvägen 68",
                Role.EMPLOYEE,
                salary5,
                List.of());
        salary5.setEmployee(employee5);

        return args -> {
            employeeRepository.save(employee1);
            employeeRepository.save(employee2);
            employeeRepository.save(employee3);
            employeeRepository.save(employee4);
            employeeRepository.save(employee5);
        };
    }

    @Bean
    public CommandLineRunner initJobDatabase(JobRepository jobRepository) {
        return args -> {
            LocalDateTime date = LocalDateTime.parse("2023-11-24T12:00");
            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.EIGHT,
                    JobStatus.PENDING,
                    55,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(1L).get(),
                    customerRepository.findById(1L).get()));

            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.TEN,
                    JobStatus.PENDING,
                    50,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(1L).get(),
                    customerRepository.findById(2L).get()));

            jobRepository.save(new Job(
                    Jobtype.BASIC,
                    date,
                    TimeSlots.THIRTEEN,
                    JobStatus.PENDING,
                    55,
                    PaymentOption.KLARNA,
                    employeeRepository.findById(2L).get(),
                    customerRepository.findById(1L).get()));
        };
    }


}
