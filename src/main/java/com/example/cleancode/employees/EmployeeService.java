package com.example.cleancode.employees;

import com.example.cleancode.exceptions.PersonAlreadyExistsException;
import com.example.cleancode.exceptions.PersonDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Long addEmployee(CreateEmployeeDTO employeeDTO) {
        Optional<Employee> optEmp = employeeRepository.findBySsNumber(employeeDTO.getSsNumber());
        if (optEmp.isEmpty()) {
            Employee employee = new Employee(
                    employeeDTO.getFirstName(),
                    employeeDTO.getLastName(),
                    employeeDTO.getPassword(),
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

        if (optEmp.isPresent()) {
            employeeRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no employee with that id in database.");
        }

    }

    public GetEmployeeDTO getEmployee(Long empId) {
        Optional<Employee> optEmp = employeeRepository.findById(empId);
        if (optEmp.isPresent()) {
            return employeeToGetEmployeeDto(optEmp.get());
        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }
    }

    public List<GetEmployeeDTO> getAllEmployees() {

        List<Employee> empList = employeeRepository.findAll();
        return empList
                .stream()
                .map(emp -> employeeToGetEmployeeDto(emp))
                .collect(Collectors.toList());

    }

    private GetEmployeeDTO employeeToGetEmployeeDto(Employee employee) {
        return new GetEmployeeDTO(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getSsNumber(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getRole());
    }


}
