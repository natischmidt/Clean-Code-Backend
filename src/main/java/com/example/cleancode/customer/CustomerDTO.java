package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private String firstName;
    private String lastName;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String adress;
    private CustomerType costumerType;
}
