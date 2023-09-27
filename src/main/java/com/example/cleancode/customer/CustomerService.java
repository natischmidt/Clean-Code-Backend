package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer(
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getSsNumber(),
                customerDTO.getEmail(),
                customerDTO.getPhoneNumber(),
                customerDTO.getAdress(),
                customerDTO.getCustomerType());
//        customer.setFirstName(customerDTO.getFirstName());
//        customer.setLastName(customerDTO.getLastName());
//        customer.setSsNumber(customerDTO.getSsNumber());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setPhoneNumber(customerDTO.getPhoneNumber());
//        customer.setAdress(customerDTO.getAdress());
//        customer.setCustomerType(CustomerType.BUSINESS);
        customerRepository.save(customer);

        return customerDTO;
    }


}
