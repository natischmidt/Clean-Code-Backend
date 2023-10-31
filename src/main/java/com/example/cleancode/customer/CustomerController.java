package com.example.cleancode.customer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public CustomerAuthenticationResponseDTO createCustomer(@RequestBody CreateCustomerDTO createDTO){
        System.out.println(createDTO);
        return customerService.createCustomer(createDTO);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/delete/{id}")
    public String removeCustomer(@PathVariable String id){
        return customerService.deleteCustomer(UUID.fromString(id));
    }

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @PatchMapping("/update/{id}")
    public CustomerDTO changeCustomerInfo(@PathVariable String id,
                                          @RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomerInfo(UUID.fromString(id), customerDTO);
    }

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/all")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(UUID.fromString(id));
    }

    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @GetMapping("/getIdByEmail/{email}")
    public UUID getCustomerByEmail(@PathVariable String email){
        return customerService.getCustomerByEmail(email);
    }
}
