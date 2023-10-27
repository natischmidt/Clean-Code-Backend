package com.example.cleancode.authentication;

import com.example.cleancode.authentication.dto.AdminTokenExtractionObject;
import com.example.cleancode.authentication.dto.CreateUserDTO;
import com.example.cleancode.authentication.dto.CreateUserRequest;
import com.example.cleancode.authentication.dto.Credentials;
import com.example.cleancode.exceptions.HttpRequestFailedException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class KeycloakService {

/*  client id for cleanCode: 16b58469-6ca4-46ea-b4bf-2b15600c5dd9

//  "id": "1bdc1c3c-4cc2-44fc-9c3c-9c630a71ea15",
//  "name": "customer",
//  "description": "customer role",
//
//  "id": "d05f878c-1dc9-401a-bc46-38957fc7f7f4",
//  "name": "employee",
//  "description": "employee role",
//
//  "id": "f3c54b32-9d24-4d05-9fd3-3a326a5be6eb",
//  "name": "admin",
//  "description": "admin role",


    /** Get an admin token from master realm. This is needed for most fetches.*/
    public String getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username", "admin");
            map.add("password", "l?3t5!C1eAn..tHäc0De.-");
            map.add("grant_type", "password");
            map.add("client_id", "admin-cli");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<AdminTokenExtractionObject> response = restTemplate.exchange(
                    "http://stadafint.se/realms/master/protocol/openid-connect/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            return Objects.requireNonNull(response.getBody()).getAccess_token();
        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Fetch failed");
        }
    }


    /**  Create a user in Keycloak. We set password as username. If successful, "201 CREATED"  is returned*/
    public String createUser(CreateUserDTO createUserDTO) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + createUserDTO.getToken());

            Credentials[] credentialsArray = {
                    new Credentials(
                            "password",
                            createUserDTO.getPassword(),
                            false
                    )};
            CreateUserRequest createUserRequest = new CreateUserRequest(
                    true,
                    createUserDTO.getEmail(),
                    createUserDTO.getFirstName(),
                    createUserDTO.getLastName(),
                    createUserDTO.getEmail(),
                    credentialsArray
            );

            HttpEntity<CreateUserRequest> entity = new HttpEntity<>(createUserRequest, headers);
            ResponseEntity<?> response = restTemplate.exchange(
                    "http://stadafint.se/admin/realms/cleanCode/users",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });

            return response.getStatusCode().toString();

        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Fetch failed");
        }
    }

    public String getUserId() {

        return null;
    }


    public String assignRoleToUser() {
        RestTemplate restTemplate = new RestTemplate();
        //måste fixa fetch för att hämta user ID innan vi kan göra denna

        //http://stadafint.se/admin/realms/cleanCode/users/ace98e7b-f596-4936-b46e-36b5ee565b25/role-mappings/clients/0fc8c1c1-7ca8-40b9-8655-bc3a48e95540

        return null;
    }

}
