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

        /** First calls checkDTO method to verify entered information. If something is wrong, we throw an exception.
         *...
         * Then creates the employee, and returns the id. If an employee with that id already exists, we throw an exception. */

        if (!checkCreateEmployeeDTO(employeeDTO)) {
            throw new InvalidRequestException("One or more fields have invalid or missing content.");
        }

        Optional<Employee> optEmp = employeeRepository.findBySsNumber(employeeDTO.getSsNumber());
        if (optEmp.isEmpty()) {
            Salary salary = new Salary(employeeDTO.getSalary());
            Employee employee = new Employee(
                    employeeDTO.getFirstName(),
                    employeeDTO.getLastName(),
                    employeeDTO.getPassword(),
                    employeeDTO.getSsNumber(),
                    employeeDTO.getEmail(),
                    employeeDTO.getPhoneNumber(),
                    employeeDTO.getAddress(),
                    employeeDTO.getCity(),
                    employeeDTO.getPostalCode(),
                    employeeDTO.getRole(),
                    salary,
                    List.of());
            salary.setEmployee(employee);
            employeeRepository.save(employee);
            return employee.getId();
        } else {
            throw new PersonAlreadyExistsException("That person is already registered as an employee.");
        }
    }

    public Long deleteEmployee(Long id) {
        /** Deletes the employee with the entered id. If no such employee exist, we throw an exception. Returns the deleted employees id.*/
        Optional<Employee> optEmp = employeeRepository.findById(id);
        if (optEmp.isPresent()) {
            employeeRepository.deleteById(id);
            return id;
        } else {
            throw new PersonDoesNotExistException("There is no employee with that id in database.");
        }
    }

    public GetEmployeeDTO getEmployee(Long empId) {
        /** Returns DTO with the employee with the entered id. If no such employee exist, we throw an exception. */
        Optional<Employee> optEmp = employeeRepository.findById(empId);
        if (optEmp.isPresent()) {
            return employeeToGetEmployeeDto(optEmp.get());
        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }
    }

    public List<GetEmployeeDTO> getAllEmployees() {
        /** Returns a list, containing all employees, including Admins */
        List<Employee> empList = employeeRepository.findAll();
        return empList
                .stream()
                .map(emp -> employeeToGetEmployeeDto(emp))
                .collect(Collectors.toList());
    }

    public GetEmployeeDTO editEmployee(Long empId, EditEmployeeDTO employeeDTO) {
        /** If an employee with the entered id exists, we update it with the values from the DTO. If not, exception is thrown. */
        Optional<Employee> optEmp = employeeRepository.findById(empId);

        if (!checkEditEmployeeDTO(employeeDTO)) {
            throw new InvalidRequestException("Some fields had missing or invalid data");
        }

        if (optEmp.isPresent()) {
            optEmp.get().setFirstName(employeeDTO.getFirstName());
            optEmp.get().setLastName(employeeDTO.getLastName());
            optEmp.get().setPassword(employeeDTO.getPassword());
            optEmp.get().setSsNumber(employeeDTO.getSsNumber());
            optEmp.get().setEmail(employeeDTO.getEmail());
            optEmp.get().setPhoneNumber(employeeDTO.getPhoneNumber());
            optEmp.get().setAddress(employeeDTO.getAddress());
            optEmp.get().setRole(employeeDTO.getRole());
            optEmp.get().getSalary().setHourlySalary(employeeDTO.getHourlySalary());
        } else {
            throw new PersonDoesNotExistException("No employee with that id was found!");
        }
        employeeRepository.save(optEmp.get());
        return employeeToGetEmployeeDto(optEmp.get());
    }

    public SalaryDTO getSalary(Long id) {
        Optional<Employee> optEmp = employeeRepository.findById(id);

        SalaryDTO salaryDTO;

        if (optEmp.isPresent()) {
            salaryDTO = new SalaryDTO(
                    optEmp.get().getSalary().getWorkedHours(),
                    optEmp.get().getSalary().getHourlySalary()
            );
        } else {
            salaryDTO = new SalaryDTO();
        }
        return salaryDTO;
    }

    private GetEmployeeDTO employeeToGetEmployeeDto(Employee employee) {
        /** Converts Employee to GetEmployeeDto*/
        return new GetEmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getSsNumber(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getCity(),
                employee.getPostalCode(),
                employee.getRole(),
                employee.getSalary().getHourlySalary());
    }

    private Boolean checkCreateEmployeeDTO(CreateEmployeeDTO dto) {
        /** Checks so that fields are not null, and that email and phone number are valid formats. Returns true if all fields are ok.*/
        return dto.getFirstName() != null
                && dto.getLastName() != null
                && dto.getPassword() != null
                && dto.getSsNumber() != null
                && dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && dto.getPhoneNumber().matches("^(\\d+){8,12}$")
                && dto.getAddress() != null
                && dto.getRole() != null;
    }

    private Boolean checkEditEmployeeDTO(EditEmployeeDTO dto) {
        /** Checks so that fields are not null, and that email and phone number are valid formats. Returns true if all fields are ok.*/
        return dto.getFirstName() != null
                && dto.getLastName() != null
                && dto.getPassword() != null
                && dto.getSsNumber() != null
                && dto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && dto.getPhoneNumber().matches("^(\\d+){8,12}$")
                && dto.getAddress() != null
                && dto.getRole() != null
                && dto.getHourlySalary() != 0;
    }


}
