package com.example.cleancode.job;

import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.example.cleancode.enums.TimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateJobDTO {

    private Jobtype jobtype;
    private String date;
    private List<TimeSlots> timeSlotList;
    private int squareMeters;
    private PaymentOption paymentOption;
    private UUID customerId;
}
