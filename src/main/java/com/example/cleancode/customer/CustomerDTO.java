package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    String firstName;
    String lastName;
    String ssNumber;
    String email;
    String phoneNumber;
    String adress;
    CustomerType customerType;
}
