package com.example.cleancode.employees;

import com.example.cleancode.exceptions.InvalidRequestException;
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

    public Long createEmployee(CreateEmployeeDTO employeeDTO) {

        if (!checkDTO(employeeDTO)) {
            throw new InvalidRequestException("One or more fields have invalid or missing content.");
        }

        Optional<Employee> optEmp = employeeRepository.findBySsNumber(employeeDTO.getSsNumber());
        if (optEmp.isEmpty()) {
            Employee employee = new Employee(
                    employeeDTO.getFirstName(),
                    employeeDTO.getLastName(),
                    employeeDTO.getPassword(),
                    employeeDTO.getSsNumber(),
                    employeeDTO.getEmail(),
                    employeeDTO.getPhoneNumber(),
                    employeeDTO.getAddress(),
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

    public GetEmployeeDTO editEmployee(EditEmployeeDTO employeeDTO) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeDTO.getId());

        if (optEmp.isPresent()) {
            optEmp.get().setFirstName(employeeDTO.getFirstName());
            optEmp.get().setLastName(employeeDTO.getLastName());
            optEmp.get().setPassword(employeeDTO.getPassword());
            optEmp.get().setSsNumber(employeeDTO.getSsNumber());
            optEmp.get().setEmail(employeeDTO.getEmail());
            optEmp.get().setPhoneNumber(employeeDTO.getPhoneNumber());
            optEmp.get().setAddress(employeeDTO.getAddress());
            optEmp.get().setRole(employeeDTO.getRole());

        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }

        employeeRepository.save(optEmp.get());
        return employeeToGetEmployeeDto(optEmp.get());
    }

    private GetEmployeeDTO employeeToGetEmployeeDto(Employee employee) {
        return new GetEmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getSsNumber(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getRole());
    }

    private Boolean checkDTO(CreateEmployeeDTO dto) {
        //Checks so that fields are not null, and that email and phone number are valid formats.

        return dto.getFirstName() != null
                && dto.getLastName() != null
                && dto.getPassword() != null
                && dto.getSsNumber() != null
                && dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && dto.getPhoneNumber().matches("^\\D*(\\d\\D*){8,12}$")
                && dto.getAddress() != null
                && dto.getRole() != null;
    }
}
