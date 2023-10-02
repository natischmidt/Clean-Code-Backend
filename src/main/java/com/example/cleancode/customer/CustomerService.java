package com.example.cleancode.customer;

import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.exceptions.CustomerAlreadyExistsException;
import com.example.cleancode.exceptions.CustomerDoesNotExistException;
import com.example.cleancode.exceptions.CustomerInfoMissmatchException;
import com.example.cleancode.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
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
        customerDTO.setCompanyName(customer.getCompanyName());
        customerDTO.setOrgNumber(customer.getOrgNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAdress(customer.getAdress());
        customerDTO.setCustomerType(customer.getCustomerType());

        return customerDTO;
    }
    @Autowired
    private EmailService emailService;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        Optional<Customer> optCustEmail = customerRepository.findByEmail(customerDTO.getEmail());
        //om det är en business så får vi ett värde i company fältet, annars null

        if (optCustEmail.isPresent()){
            throw new CustomerAlreadyExistsException(customerDTO.getEmail());
        }

        try {
            if (customerDTO.getCompanyName() == null && customerDTO.getOrgNumber() == null){
                Customer customer = new Customer(
                        customerDTO.getFirstName(),
                        customerDTO.getLastName(),
                        customerDTO.getEmail(),
                        customerDTO.getPhoneNumber(),
                        customerDTO.getAdress(),
                        CustomerType.PRIVATE);

                customerRepository.save(customer);

                emailService.sendEmail(customerDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");

                return customerDTO;
            }
            if (customerDTO.getCompanyName() != null && customerDTO.getOrgNumber() != null){
                Customer customer = new Customer(
                        customerDTO.getFirstName(),
                        customerDTO.getLastName(),
                        customerDTO.getCompanyName(),
                        customerDTO.getOrgNumber(),
                        customerDTO.getEmail(),
                        customerDTO.getPhoneNumber(),
                        customerDTO.adress,
                        CustomerType.BUSINESS);

                customerRepository.save(customer);

                emailService.sendEmail(customerDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");

                return customerDTO;
            }
            if(customerDTO.getCompanyName() != null && customerDTO.getOrgNumber() == null){
                throw new CustomerInfoMissmatchException("If companyName isn't null, you need a orgNumber");
            }
            if (customerDTO.getCompanyName() == null && customerDTO.getOrgNumber() != null){
                throw new CustomerInfoMissmatchException("if orgNumber isn't null, you need a company name");
            }
            // ^ dom klagar i if-satserna, men tycker dom ser nice ut...
        } catch (Exception e){
            throw new RuntimeException("ERROR -->" + e.getMessage());
        }
        throw new CustomerInfoMissmatchException("Should not get this :(");
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
            if (customerDTO.getCompanyName() != null){
                customerUpdate.setCompanyName(customerDTO.getCompanyName());
            }
            if (customerDTO.getOrgNumber() != null){
                customerUpdate.setOrgNumber(customerDTO.getOrgNumber());
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

    // lägga till filter för
}
