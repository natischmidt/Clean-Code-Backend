package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import com.example.cleancode.job.Job;
import lombok.Data;

import java.util.List;

@Data
public class EditEmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;

}
