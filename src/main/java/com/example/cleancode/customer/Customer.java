package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    private UUID id;

    private String firstName;
    private String lastName;
    private String password;
    private String companyName;
    private String orgNumber;
    private String email;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

    public Customer(UUID id, String firstName, String lastName, String password, String companyName, String orgNumber, String email, String phoneNumber, String address, CustomerType customerType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.companyName = companyName;
        this.orgNumber = orgNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.customerType = customerType;
    }
    public Customer(UUID id, String firstName, String lastName, String password, String email, String phoneNumber, String address, CustomerType customerType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.customerType = customerType;
    }
}
