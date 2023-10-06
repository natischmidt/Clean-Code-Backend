package com.example.cleancode.job;

import com.example.cleancode.enums.Jobtype;
import com.example.cleancode.enums.PaymentOption;
import com.example.cleancode.enums.TimeSlots;
import lombok.Data;
import java.util.List;

@Data
public class CreateJobDTO {

    private Jobtype jobtype;
    private String date;
    private List<TimeSlots> timeSlotList;
    private int squareMeters;
    private PaymentOption paymentOption;
    private Long customerId;
//    private Long empId;
}
