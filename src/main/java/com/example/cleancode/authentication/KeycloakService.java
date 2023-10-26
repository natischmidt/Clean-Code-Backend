package com.example.cleancode.authentication;

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

    public String getAdminToken() throws Exception {
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
            ResponseEntity<Object> response = restTemplate.exchange(
                    "http://stadafint.se/realms/master/protocol/openid-connect/token",
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return Objects.requireNonNull(response.getBody()).toString().substring(14, response.getBody().toString().indexOf(','));
        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Fetch failed");
        }
    }

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

        return response.toString();

        } catch (HttpRequestFailedException e) {
            throw new HttpRequestFailedException("Fetch failed");
        }
    }
}
