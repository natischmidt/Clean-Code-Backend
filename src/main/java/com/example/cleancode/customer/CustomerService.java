package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.exceptions.CustomerAlreadyExistsException;
import com.example.cleancode.exceptions.CustomerDoesNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    public CustomerDTO customerEntityToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setSsNumber(customer.getSsNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAdress(customer.getAdress());
        customerDTO.setCustomerType(customer.getCustomerType());

        return customerDTO;
    }
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Optional<Customer> optCust = customerRepository.findBySsNumber(customerDTO.getSsNumber());
        if (optCust.isEmpty()){
            Customer customer = new Customer(
                    customerDTO.getFirstName(),
                    customerDTO.getLastName(),
                    customerDTO.getSsNumber(),
                    customerDTO.getEmail(),
                    customerDTO.getPhoneNumber(),
                    customerDTO.getAdress(),
                    customerDTO.getCustomerType());

            customerRepository.save(customer);
        } else {
            throw new CustomerAlreadyExistsException("Customer with the chosen SS-Number already exists");
        }
        return customerDTO;
    }


    public String deleteCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            customerRepository.deleteById(id);
            return "Customer with the ID: " + id + " have been removed";
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    @Transactional
    public CustomerDTO updateCustomerInfo(Long id, CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            Customer customerUpdate = optionalCustomer.get();
            if(customerDTO.getFirstName() != null){
                customerUpdate.setFirstName(customerDTO.getFirstName());
            }
            if (customerDTO.getLastName() != null){
                customerUpdate.setLastName(customerDTO.getLastName());
            }
            if (customerDTO.getSsNumber() != null){
                customerUpdate.setSsNumber(customerDTO.getSsNumber());
            }
            if (customerDTO.getEmail() != null){
                customerUpdate.setEmail(customerDTO.getEmail());
            }
            if (customerDTO.getPhoneNumber() != null){
                customerUpdate.setPhoneNumber(customerDTO.getPhoneNumber());
            }
            if (customerDTO.getAdress() != null){
                customerUpdate.setAdress(customerDTO.getAdress());
            }if (customerDTO.getCustomerType() != null){
                customerUpdate.setCustomerType(customerDTO.getCustomerType());
            }
            customerRepository.save(customerUpdate);
            return customerEntityToDTO(customerUpdate);
        }
        else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        List<CustomerDTO> allCustomersDTO = new ArrayList<>();
        for (Customer customer : allCustomers){
            allCustomersDTO.add(customerEntityToDTO(customer));
        }
        return allCustomersDTO;
    }

    public CustomerDTO getCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            return customerEntityToDTO(optionalCustomer.get());
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }
}
