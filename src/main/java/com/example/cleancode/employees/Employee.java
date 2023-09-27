package com.example.cleancode.employees;

import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String ssNumber;
    private String email;
    private int phoneNumber;
    private String adress;
    @Enumerated(EnumType.STRING)
    private Role role;


    public Employee(String firstName,
                    String lastName,
                    String ssNumber,
                    String email,
                    int phoneNumber,
                    String adress,
                    Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssNumber = ssNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.role = role;
    }
}
