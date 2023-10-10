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
import java.util.UUID;

@Service
public class CustomerService {

    public CustomerDTO customerEntityToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setCompanyName(customer.getCompanyName());
        customerDTO.setOrgNumber(customer.getOrgNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCustomerType(customer.getCustomerType());

        return customerDTO;
    }

    @Autowired
    private EmailService emailService;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CreateCustomerDTO createCustomer(CreateCustomerDTO createDTO) {

        Optional<Customer> optCustEmail = customerRepository.findByEmail(createDTO.getEmail());
        //om det är en business så får vi ett värde i company fältet, annars null

        if (optCustEmail.isPresent()){
            throw new CustomerAlreadyExistsException(createDTO.getEmail());
        }

        try {
            if (createDTO.getCompanyName() == null && createDTO.getOrgNumber() == null){
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        CustomerType.PRIVATE);


                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");

                return createDTO;
            }
            if (createDTO.getCompanyName() != null && createDTO.getOrgNumber() != null){
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getCompanyName(),
                        createDTO.getOrgNumber(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.address,
                        CustomerType.BUSINESS);
                customer.setPassword(createDTO.getPassword());
                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");

                return createDTO;
            }
            if(createDTO.getCompanyName() != null && createDTO.getOrgNumber() == null){
                throw new CustomerInfoMissmatchException("If companyName isn't null, you need a orgNumber");
            }
            if (createDTO.getCompanyName() == null && createDTO.getOrgNumber() != null){
                throw new CustomerInfoMissmatchException("if orgNumber isn't null, you need a company name");
            }
            // ^ dom klagar i if-satserna, men tycker dom ser nice ut...
        } catch (Exception e){
            throw new RuntimeException("ERROR -->" + e.getMessage());
        }
        throw new CustomerInfoMissmatchException("Should not get this :(");
    }


    public String deleteCustomer(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            customerRepository.deleteById(id);
            return "Customer with the ID: " + id + " have been removed";
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    @Transactional
    public CustomerDTO updateCustomerInfo(UUID id, CustomerDTO customerDTO) {
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
            if (customerDTO.getAddress() != null){
                customerUpdate.setAddress(customerDTO.getAddress());
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

    public CustomerDTO getCustomerById(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()){
            return customerEntityToDTO(optionalCustomer.get());
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    // lägga till filter för
}
