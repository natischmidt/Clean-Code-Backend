package com.example.cleancode.employees;

import com.example.cleancode.exceptions.PersonAlreadyExistsException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;
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
                    employeeDTO.getRole(),
                    List.of());

            employeeRepository.save(employee);
            return employee.getId();
        } else {
            throw new PersonAlreadyExistsException("That person is already registered as an employee.");
        }
    }

    public Long deleteEmployee(Long id) {
        Optional<Employee> optEmp = employeeRepository.findById(id);

        if(optEmp.isPresent()) {
            employeeRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no employee with that id in database.");
        }

    }
}
