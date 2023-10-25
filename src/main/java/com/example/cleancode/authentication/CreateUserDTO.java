package com.example.cleancode.authentication;

import lombok.Data;

@Data
public class CreateUserDTO {

    private boolean enabled;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String credentials;
    private String password;
    private String token;


}
