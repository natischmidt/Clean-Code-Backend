package com.example.cleancode.customer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerAuthenticationResponseDTO {

    private String jwt;
    private String userId;

}
