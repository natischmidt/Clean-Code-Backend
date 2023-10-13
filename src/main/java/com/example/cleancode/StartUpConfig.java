package com.example.cleancode;

import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.employees.Salary;
import com.example.cleancode.enums.*;
import com.example.cleancode.job.CreateJobDTO;
import com.example.cleancode.job.Job;
import com.example.cleancode.job.JobRepository;
import com.example.cleancode.job.JobService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static com.example.cleancode.enums.TimeSlots.*;

@Configuration
public class StartUpConfig {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final JobService jobService;

    public StartUpConfig(EmployeeRepository employeeRepository, CustomerRepository customerRepository, JobService jobService) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.jobService = jobService;
    }

    @Bean
    public CommandLineRunner initCustomerDatabase(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632"),
                    "Lars",
                    "Olofsson",
                    "password",
                    "Larssons måleri AB",
                    "123445660654-57439",
                    "lars.olofsson@malari.se",
                    "0730123123",
                    "Adressgatan 12",
                    CustomerType.BUSINESS

            ));
            customerRepository.save(new Customer(

                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53"),
                    "Hanna",
                    "Root",
                    "password",
                    "hanna.root@ikea.se",
                    "0730123123",
                    "Adressgatan 13",
                    CustomerType.PRIVATE
            ));
        };
    }

    @Bean
    public CommandLineRunner initEmployeeDatabase(EmployeeRepository employeeRepository) {
        Salary salary1 = new Salary(100);
        Employee employee1 = new Employee(
                "Kent",
                "Andersson",
                "password",
                "197404119437",
                "kent.andersson@stadafint.se",
                "0734123323",
                "Adressvägen 23",
                Role.EMPLOYEE,
                salary1,
                List.of());
        salary1.setEmployee(employee1);

        Salary salary2 = new Salary(200);
        Employee employee2 = new Employee(
                "Lisa",
                "Grönberg",
                "password",
                "199111129674",
                "lisa.gronberg@stadafint.se",
                "0702424242",
                "Adressvägen 65",
                Role.ADMIN,
                salary2,
                List.of());
        salary2.setEmployee(employee2);

        Salary salary3 = new Salary(150);
        Employee employee3 = new Employee(
                "Klas",
                "McClean",
                "password",
                "199901181373",
                "klas.mcclean@stadafint.se",
                "0732424542",
                "Adressvägen 66",
                Role.EMPLOYEE,
                salary3,
                List.of());
        salary3.setEmployee(employee3);

        Salary salary4 = new Salary(150);
        Employee employee4 = new Employee(
                "Sten",
                "Sturesson",
                "password",
                "197304165789",
                "sten.sturesson@stadafint.se",
                "0738947468",
                "Adressvägen 67",
                Role.EMPLOYEE,
                salary4,
                List.of());
        salary4.setEmployee(employee4);

        Salary salary5 = new Salary(150);
        Employee employee5 = new Employee(
                "Johanna",
                "Qvist",
                "password",
                "199712180762",
                "johanna.qvist@stadafint.se",
                "0706788263",
                "Adressvägen 68",
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
    public CommandLineRunner initJobDatabase(JobService jobService) {
        return args -> {
            List<TimeSlots> timesList = new ArrayList<>();
          LocalDateTime dateInDateFormat = LocalDateTime.parse("2023-11-24T12:00");

            String date = "2023-11-24";

            timesList.add(EIGHT);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date,
                    List.of(EIGHT),
                    55,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632")));

            timesList.clear();
            timesList.add(NINE);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date,
                    List.of(NINE),
                    55,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632")));

            timesList.clear();
            timesList.add(TEN);
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date,
                    List.of(TEN),
                    50,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53")));

            date = "2023-11-25";

            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date,
                    List.of(THIRTEEN),
                    55,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632")));

            jobService.createJob(new CreateJobDTO(
                    Jobtype.ADVANCED,
                    date,
                    List.of(FOURTEEN),
                    30,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53")));

            date = "2023-11-26";
            jobService.createJob(new CreateJobDTO(
                    Jobtype.DIAMOND,
                    date,
                    List.of(EIGHT),
                    43,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53")));

            jobService.createJob(new CreateJobDTO(
                    Jobtype.ADVANCED,
                    date,
                    List.of(TWELVE),
                    56,
                    PaymentOption.KLARNA,
                    UUID.fromString("678b8da4-28cd-42c9-b6c6-a3dc53204632")));
            jobService.createJob(new CreateJobDTO(
                    Jobtype.BASIC,
                    date,
                    List.of(SIXTEEN),
                    25,
                    PaymentOption.KLARNA,
                    UUID.fromString("e7b043d9-0264-429a-8073-c5524e914c53")));

        };
    }
}
