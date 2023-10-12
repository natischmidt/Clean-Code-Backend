package com.example.cleancode.authentication;

import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerRepository;
import com.example.cleancode.employees.Employee;
import com.example.cleancode.employees.EmployeeRepository;
import com.example.cleancode.exceptions.InvalidRequestException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public AuthService(EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    public UUID loginCustomer(AuthDTO authDTO) {
        Optional <Customer> optCustomer = customerRepository.findByEmail(authDTO.getEmail());

        if (optCustomer.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }

        if (optCustomer.get().getPassword().equals(authDTO.getPassword())) {
            return optCustomer.get().getId();
        } else {
            throw new InvalidRequestException("The given password was incorrect");
        }
    }

    public AuthResponseDTO loginEmployee(AuthDTO authDTO) {
        Optional <Employee> optEmployee = employeeRepository.findByEmail(authDTO.getEmail());

        if (optEmployee.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }

        if (optEmployee.get().getPassword().equals(authDTO.getPassword())) {
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(
                    optEmployee.get().getId(),
                    optEmployee.get().getRole()
            );
            return authResponseDTO;
        } else {
            throw new InvalidRequestException("The given password was incorrect");
        }
    }
}
