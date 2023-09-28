package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetEmployeeDTO {

    private String firstName;
    private String lastName;
    private String ssNumber;
    private String email;
    private int phoneNumber;
    private String adress;
    private Role role;
}
