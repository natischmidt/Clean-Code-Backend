package com.example.cleancode.job;

import com.example.cleancode.availability.Availability;
import com.example.cleancode.customer.Customer;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long jobId;
    @Enumerated(EnumType.STRING)
    private Jobtype jobtype;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    private int squareMeters;
    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @ManyToMany(mappedBy = "jobs")
    private List<Availability> availabilities;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "cus_id")
    @JsonIgnore
    private Customer customer;

    public Job(
            Jobtype jobtype,
            String dateAndTime,
            JobStatus jobStatus,
            int squareMeters,
            PaymentOption paymentOption,
            Employee employee,
            Customer customer) {
        this.jobtype = jobtype;
        this.date = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.jobStatus = jobStatus;
        this.squareMeters = squareMeters;
        this.paymentOption = paymentOption;
        this.employee = employee;
        this.customer = customer;
    }
}
