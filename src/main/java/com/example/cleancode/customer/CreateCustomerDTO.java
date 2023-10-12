package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerDTO {

    String firstName;
    String lastName;
    String password;
    String companyName;
    String orgNumber;
    String email;
    String phoneNumber;
    String address;
    CustomerType customerType;

}
