package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CreateJobDTO {

    private Jobtype jobtype;
    private String dateAndTime;
    private JobStatus jobStatus;
    private int squareMeters;
    private PaymentOption paymentOption;
    private Long customerId;
}
