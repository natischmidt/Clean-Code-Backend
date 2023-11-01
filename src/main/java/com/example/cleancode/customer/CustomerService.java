package com.example.cleancode.customer;

import com.example.cleancode.authentication.KeycloakService;
import com.example.cleancode.authentication.dto.CreateUserDTO;
import com.example.cleancode.enums.CustomerType;
import com.example.cleancode.exceptions.*;
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

    private final KeycloakService keycloakService;


    public CustomerDTO customerEntityToDTO(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setCompanyName(customer.getCompanyName());
        customerDTO.setOrgNumber(customer.getOrgNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPostalCode(customer.getPostalCode());
        customerDTO.setCustomerType(customer.getCustomerType());

        return customerDTO;
    }

    @Autowired
    private EmailService emailService;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, KeycloakService keycloakService) {
        this.customerRepository = customerRepository;
        this.keycloakService = keycloakService;
    }

    public CustomerAuthenticationResponseDTO createCustomer(CreateCustomerDTO createDTO) {

        Optional<Customer> optCustEmail = customerRepository.findByEmail(createDTO.getEmail());
        //om det är en business så får vi ett värde i company fältet, annars null

        if (optCustEmail.isPresent()) {
            throw new CustomerAlreadyExistsException(createDTO.getEmail());
        }

        if (createDTO.getFirstName() != "") {
            if (!checkCreateCustomerDTO(createDTO)) {
                throw new InvalidRequestException("Some fields had incorrect or missing information.");
            }
        }

        String keycloakResponse = keycloakService.createUser(new CreateUserDTO(createDTO.getEmail(), createDTO.getFirstName(), createDTO.getLastName(), createDTO.getPassword()));
        if(!keycloakResponse.equals("201 CREATED")) {
            throw new HttpRequestFailedException("Failed to create user in keycloak step 1.");
        }

        String adminToken = keycloakService.getAdminToken();

        try {
            keycloakService.assignRoleToUser("customer", createDTO.getEmail(), adminToken);
        } catch (HttpRequestFailedException e){
            throw new HttpRequestFailedException("Failed to get userId or failed to assign role to user");
        }

        try {
            if (createDTO.getCompanyName().isEmpty() && createDTO.getOrgNumber().isEmpty()) {
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        createDTO.getCity(),
                        createDTO.getPostalCode(),
                        CustomerType.PRIVATE);

                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "You are now a registered member at StädaFintAB. Welcome!");

                return new CustomerAuthenticationResponseDTO(keycloakService.getUserToken(createDTO.getEmail(), createDTO.getPassword()), customer.getId().toString());
            }

            if (!createDTO.getCompanyName().isEmpty() && !createDTO.getOrgNumber().isEmpty()) {
                Customer customer = new Customer(
                        UUID.randomUUID(),
                        createDTO.getFirstName(),
                        createDTO.getLastName(),
                        createDTO.getPassword(),
                        createDTO.getCompanyName(),
                        createDTO.getOrgNumber(),
                        createDTO.getEmail(),
                        createDTO.getPhoneNumber(),
                        createDTO.getAddress(),
                        createDTO.getCity(),
                        createDTO.getPostalCode(),
                        CustomerType.BUSINESS);
                customer.setPassword(createDTO.getPassword());
                customerRepository.save(customer);

                emailService.sendEmail(createDTO.getEmail(),
                        "StädaFint AB",
                        "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");
                return new CustomerAuthenticationResponseDTO(keycloakService.getUserToken(createDTO.getEmail(), createDTO.getPassword()), customer.getId().toString());
            }

            // andra delen av "if condition" är överflödig, eftersom annars hamnar vi i någon av return ovan, men det är tydligare att behålla såhär
            if (!createDTO.getCompanyName().isEmpty() && createDTO.getOrgNumber().isEmpty()) {
                throw new CustomerInfoMissMatchException("If companyName isn't null, you need a orgNumber");
            }

            throw new CustomerInfoMissMatchException("if orgNumber isn't null, you need a company name");

//             ^ dom klagar i if-satserna, men tycker dom ser nice ut...
        } catch (Exception e) {
            throw new RuntimeException("ERROR -->" + e.getMessage());
        }
    }

    public String deleteCustomer(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            customerRepository.deleteById(id);
            return "Customer with the ID: " + id + " have been removed";
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    @Transactional
    public CustomerDTO updateCustomerInfo(UUID id, CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customerUpdate = optionalCustomer.get();
            if (customerDTO.getFirstName() != null) {
                customerUpdate.setFirstName(customerDTO.getFirstName());
            }
            if (customerDTO.getLastName() != null) {
                customerUpdate.setLastName(customerDTO.getLastName());
            }
            if (customerDTO.getCompanyName() != null && customerUpdate.getCustomerType().equals(CustomerType.BUSINESS)) {
                customerUpdate.setCompanyName(customerDTO.getCompanyName());
            }
            if (customerDTO.getOrgNumber() != null && customerUpdate.getCustomerType().equals(CustomerType.BUSINESS)) {
                customerUpdate.setOrgNumber(customerDTO.getOrgNumber());
            }
            if (customerDTO.getEmail() != null) {
                customerUpdate.setEmail(customerDTO.getEmail());
            }
            if (customerDTO.getPhoneNumber() != null) {
                customerUpdate.setPhoneNumber(customerDTO.getPhoneNumber());
            }
            if (customerDTO.getAddress() != null) {
                customerUpdate.setAddress(customerDTO.getAddress());
            }
            if (customerDTO.getCity() != null) {
                customerUpdate.setCity(customerDTO.getCity());
            }
            if (customerDTO.getPostalCode() != null) {
                customerUpdate.setPostalCode(customerDTO.getPostalCode());
            }
            if (customerDTO.getCustomerType() != null) {
                customerUpdate.setCustomerType(customerDTO.getCustomerType());
            }
            customerRepository.save(customerUpdate);
            return customerEntityToDTO(customerUpdate);
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        List<CustomerDTO> allCustomersDTO = new ArrayList<>();
        for (Customer customer : allCustomers) {
            allCustomersDTO.add(customerEntityToDTO(customer));
        }
        return allCustomersDTO;
    }

    public CustomerDTO getCustomerById(UUID id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            return customerEntityToDTO(optionalCustomer.get());
        } else {
            throw new CustomerDoesNotExistException(id);
        }
    }

    public UUID getCustomerByEmail(String email) {
        UUID optEmail = customerRepository.findByEmail(email).get().getId();

        if (optEmail != null) {
            return optEmail;
        } else {
            throw new CustomerWithEmailDoesNotExistException(email);
        }
    }

    private boolean checkCreateCustomerDTO(CreateCustomerDTO createCustomerDTO) {
        System.out.println(createCustomerDTO.getFirstName());
        System.out.println(createCustomerDTO.getLastName());
        System.out.println(createCustomerDTO.getPassword());
        System.out.println(createCustomerDTO.getEmail());
        System.out.println(createCustomerDTO.getPhoneNumber());
        System.out.println(createCustomerDTO.getAddress());

        return createCustomerDTO.getFirstName() != null
                && createCustomerDTO.getLastName() != null
                && (createCustomerDTO.getPassword() != null && createCustomerDTO.getPassword().length() >= 8)
                && createCustomerDTO.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && createCustomerDTO.getPhoneNumber().matches("^(\\d+){8,12}$")
                && createCustomerDTO.getAddress() != null;
    }
}
