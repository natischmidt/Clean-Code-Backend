package com.example.cleancode.authentication.dto;

import lombok.Data;

@Data
public class CreateUserDTO {

    private boolean enabled;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String token;


}
