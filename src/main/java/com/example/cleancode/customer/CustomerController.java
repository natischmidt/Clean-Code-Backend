package com.example.cleancode.customer;

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
    public CreateCustomerDTO createCustomer(@RequestBody CreateCustomerDTO createDTO){
        return customerService.createCustomer(createDTO);
    }
    @DeleteMapping("/delete/{id}")
    public String removeCustomer(@PathVariable UUID id){
        return customerService.deleteCustomer(id);
    }
    @PatchMapping("/update/{id}")
    public CustomerDTO changeCustomerInfo(@PathVariable UUID id,
                                          @RequestBody CustomerDTO customerDTO){
        return customerService.updateCustomerInfo(id, customerDTO);
    }
    @GetMapping("/all")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable UUID id){
        return customerService.getCustomerById(id);
    }

}
