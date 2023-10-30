package com.example.cleancode.authentication;

import com.example.cleancode.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {

    private Long id;
    private Role role;
    private String jwt;

}
