package com.example.cleancode.employees;

import com.example.cleancode.booked.Booked;
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
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;
    private String lastName;
    private String password;
    private String ssNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "employees")
    private List<Booked> availabilities;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Salary salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobList = new ArrayList<>();

    public Employee(String firstName, String lastName, String password, String ssNumber, String email,
                    String phoneNumber, String address, String city, String postalCode, Role role, Salary salary, List<Job> jobList) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.ssNumber = ssNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.role = role;
        this.jobList = jobList;
        this.salary = salary;
    }
}
