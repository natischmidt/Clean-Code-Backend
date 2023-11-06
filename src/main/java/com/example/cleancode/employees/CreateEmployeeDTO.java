package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import com.example.cleancode.job.Job;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateEmployeeDTO {

    private String firstName;
    private String lastName;
    private String password;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;
    private Role role;
    private int salary;
//    private List<Job> jobList = new ArrayList<>();

}
