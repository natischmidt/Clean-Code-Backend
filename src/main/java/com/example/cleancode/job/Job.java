package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private Date date;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    private int squareMeters;
    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    public Job(Jobtype jobtype, Date date, JobStatus jobStatus, int squareMeters, PaymentOption paymentOption) {
        this.jobtype = jobtype;
        this.date = date;
        this.jobStatus = jobStatus;
        this.squareMeters = squareMeters;
        this.paymentOption = paymentOption;
    }
}
