package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetEmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
    private int salary;
}
