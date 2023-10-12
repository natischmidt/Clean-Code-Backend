package com.example.cleancode.job;

import lombok.Data;

@Data
public class GetAvailableEmployeeDTO {

    private String date;
    private int lookForAvailableThisManyHours;

}
