package com.example.cleancode.employees;

import com.example.cleancode.enums.Role;
import com.example.cleancode.job.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String password;
    private String ssNumber;
    private String email;
    private int phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobList = new ArrayList<>();

    public Employee(String firstName,
                    String lastName,
                    String password,
                    String ssNumber,
                    String email,
                    int phoneNumber,
                    String address,
                    Role role,
                    List<Job> jobList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.ssNumber = ssNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.jobList = jobList;
    }
}
