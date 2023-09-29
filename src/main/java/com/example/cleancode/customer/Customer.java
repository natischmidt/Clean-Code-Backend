package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;
    private String password;
    private String companyName;
    private String orgNumber; //company // org nr // utan > private , med > business
    private String email;
    private String phoneNumber;
    private String adress;
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

    public Customer(String firstName, String lastName, String companyName, String orgNumber, String email, String phoneNumber, String adress, CustomerType customerType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.orgNumber = orgNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.customerType = customerType;
    }
    public Customer(String firstName, String lastName, String email, String phoneNumber, String adress, CustomerType customerType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.orgNumber = orgNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.customerType = customerType;
    }
}
