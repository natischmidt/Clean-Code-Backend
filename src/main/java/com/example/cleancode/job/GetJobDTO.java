package com.example.cleancode.job;

import com.example.cleancode.enums.JobStatus;
import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.example.cleancode.enums.TimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetJobDTO {
    private long jobId;
    private Jobtype jobtype;
    private LocalDateTime date;
    private TimeSlots timeSlot;
    private JobStatus jobStatus;
    private int squareMeters;
    private PaymentOption paymentOption;

}
