package com.example.cleancode.employees;

import com.example.cleancode.exceptions.PersonAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Long addEmployee(CreateEmployeeDTO employeeDTO) {
        Optional<Employee> optEmp= employeeRepository.findBySsNumber(employeeDTO.getSsNumber());
        if(optEmp.isEmpty()) {
            Employee employee = new Employee(
                    employeeDTO.getFirstName(),
                    employeeDTO.getLastName(),
                    employeeDTO.getSsNumber(),
                    employeeDTO.getEmail(),
                    employeeDTO.getPhoneNumber(),
                    employeeDTO.getAdress(),
                    employeeDTO.getRole());

            employeeRepository.save(employee);
            return employee.getId();
        } else {
            throw new PersonAlreadyExistsException("That person is already registered as an employee.");
        }
    }

}
