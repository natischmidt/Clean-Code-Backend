package com.example.cleancode.employees;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/addEmployee")
    public Long addEmployee(@RequestBody CreateEmployeeDTO employeeDTO) {
        return employeeService.addEmployee(employeeDTO);
    }

    @DeleteMapping("/deleteEmployee")
    public Long deleteEmployee(@RequestBody Long id) {
        return employeeService.deleteEmployee(id);

    }

}
