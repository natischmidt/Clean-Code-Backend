package com.example.cleancode.authentication;

import com.example.cleancode.customer.Customer;
import com.example.cleancode.customer.CustomerAuthenticationResponseDTO;
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
    private final KeycloakService keycloakService;

    public AuthService(EmployeeRepository employeeRepository, CustomerRepository customerRepository, KeycloakService keycloakService) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.keycloakService = keycloakService;
    }

    public CustomerAuthenticationResponseDTO loginCustomer(AuthDTO authDTO) {
        Optional <Customer> optCustomer = customerRepository.findByEmail(authDTO.getEmail());

        if (optCustomer.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }

        String jwt= keycloakService.getUserToken(authDTO.getEmail(), authDTO.getPassword());
        String userId = optCustomer.get().getId().toString();

        return new CustomerAuthenticationResponseDTO(jwt, userId);
//        if (optCustomer.get().getPassword().equals(authDTO.getPassword())) {
//            return optCustomer.get().getId();
//        } else {
//            throw new InvalidRequestException("The given password was incorrect");
//        }
    }

    public AuthResponseDTO loginEmployee(AuthDTO authDTO) {
        Optional <Employee> optEmployee = employeeRepository.findByEmail(authDTO.getEmail());

        if (optEmployee.isEmpty()) {
            throw new PersonDoesNotExistException("Email not found");
        }

        if (optEmployee.get().getPassword().equals(authDTO.getPassword())) {
            return new AuthResponseDTO(
                    optEmployee.get().getId(),
                    optEmployee.get().getRole()
            );
        } else {
            throw new InvalidRequestException("The given password was incorrect");
        }
    }
}
