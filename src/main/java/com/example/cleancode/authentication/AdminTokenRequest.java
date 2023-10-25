package com.example.cleancode.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminTokenRequest {

    private String grant_type;
    private String username;
    private String password;
    private String client_id;

}
