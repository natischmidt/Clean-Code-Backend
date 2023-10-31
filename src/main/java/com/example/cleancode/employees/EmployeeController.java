package com.example.cleancode.employees;

//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

//    @PreAuthorize("hasRole('admin')")
    @PostMapping("/createEmployee")
    public Long createEmployee(@RequestBody CreateEmployeeDTO employeeDTO) {
        System.out.println("***********************************" + employeeDTO);
        return employeeService.createEmployee(employeeDTO);
    }

//    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/deleteEmployee")
    public Long deleteEmployee(@RequestHeader Long empId) {
        return employeeService.deleteEmployee(empId);
    }

    // Osäker på hur vi gör här. Öppnar vi för både admin och employee så kan employees i princip hämta varandras info, men employees behöver kunna hämta sin egen info
//    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/getEmployee")
    public GetEmployeeDTO getEmployee(@RequestHeader Long empId) {
        return employeeService.getEmployee(empId);
    }

//        @PreAuthorize("hasRole('admin')")
    @GetMapping("/getAllEmployees")
    public List<GetEmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

//        @PreAuthorize("hasRole('admin')")
    @PutMapping("/editEmployee")
    public GetEmployeeDTO editEmployee(@RequestHeader Long empId, @RequestBody EditEmployeeDTO employee) {

        return employeeService.editEmployee(empId, employee);
    }

    // Osäker på hur vi gör här. Öppnar vi för både admin och employee så kan employees i princip hämta varandras info, men employees behöver kunna hämta sin egen info
//        @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/getSalary/{id}")
    public SalaryDTO getSalary(@PathVariable Long id) {
        return employeeService.getSalary(id);

    }

}
