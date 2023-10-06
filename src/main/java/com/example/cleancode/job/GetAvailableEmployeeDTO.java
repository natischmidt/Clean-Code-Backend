package com.example.cleancode.job;

import com.example.cleancode.enums.TimeSlots;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetAvailableEmployeeDTO {

    private String date;
    private int lookForAvailableThisManyHours;

}
