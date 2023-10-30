package com.example.cleancode.authentication;

import com.example.cleancode.authentication.dto.*;
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


    RestTemplate restTemplate;
    String cleanCodeClientId = "16b58469-6ca4-46ea-b4bf-2b15600c5dd9";
    String customerRoleId = "1bdc1c3c-4cc2-44fc-9c3c-9c630a71ea15";
    String employeeRoleId = "d05f878c-1dc9-401a-bc46-38957fc7f7f4";
    String adminRoleId = "f3c54b32-9d24-4d05-9fd3-3a326a5be6eb";
    String client_secret = "Z9ejOes6m4LVvLvBYKfkgUMdQ2MdK9Dn";


    /**
     * Get an admin token from master realm. This is needed for most fetches.
     */
    public String getAdminToken() {
        restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username", "admin");
            map.add("password", "l?3t5!C1eAn..tHäc0De.-");
            map.add("grant_type", "password");
            map.add("client_id", "admin-cli");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<TokenRequestObject> response = restTemplate.exchange(
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

    /**
     * Create a user in Keycloak. We set email as username. If successful, "201 CREATED"  is returned
     */
    public String createUser(CreateUserDTO createUserDTO) {
        restTemplate = new RestTemplate();

        String adminToken = getAdminToken();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + adminToken);

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


    /**
     * Get the Keycloak user id by username. Username is the same as password.
     * For testing purposes there is an endpoint for this, but we probably want to call this method from within spring boot only.
     **/
    public String getUserId(String username, String adminToken) {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + adminToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "http://stadafint.se/admin/realms/cleanCode/users?username=" + username;
        ResponseEntity<UserInfoObject[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        return Objects.requireNonNull(Objects.requireNonNull(response.getBody())[0].getId());

    }

    /**
     * Assign a role to the Keycloak user.
     * For testing purposes there is an endpoint for this, but we probably want to call this method from within spring boot only.
     **/
    public String assignRoleToUser(String role, String username, String adminToken) {
        restTemplate = new RestTemplate();
        String createRoleId;

        switch (role) {
            case "admin" -> createRoleId = adminRoleId;
            case "employee" -> createRoleId = employeeRoleId;
            default -> createRoleId = customerRoleId;
        }

        String userId = getUserId(username, adminToken);
        String url = "http://stadafint.se/admin/realms/cleanCode/users/" + userId + "/role-mappings/realm";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminToken);

        CreateRoleObject[] createRoleObject = {new CreateRoleObject(createRoleId, role)};

        HttpEntity<Object> entity = new HttpEntity<>(createRoleObject, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.toString();
    }

    public String getUserToken(String username, String password) {
        restTemplate = new RestTemplate();

        String url = "http://stadafint.se/realms/cleanCode/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_secret", client_secret);
        map.add("client_id", "cleanCode");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<TokenRequestObject> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                });

        return Objects.requireNonNull(response.getBody()).getAccess_token();
    }

}
