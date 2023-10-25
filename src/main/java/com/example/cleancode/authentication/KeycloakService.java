package com.example.cleancode.authentication;

import com.example.cleancode.exceptions.HttpRequestFailedException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class KeycloakService {



    public String getAdminToken () throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username", "admin");
            map.add("password", "l?3t5!C1eAn..tHäc0De.-");
            map.add("grant_type", "password");;
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

//        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", "Bearer " + createUserDTO.getToken());

            JSONObject arrayJson = new JSONObject();
            arrayJson.put("type", "password");
            arrayJson.put("value", createUserDTO.getPassword());
            arrayJson.put("temporary", false);
            JSONObject[] objectArray = new JSONObject[]{arrayJson};

            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("enabled", true);
            map.add("email", createUserDTO.getEmail());
            map.add("firstName", createUserDTO.getFirstName());
            map.add("lastName", createUserDTO.getLastName());
            map.add("username", createUserDTO.getEmail());
            map.add("credentials", objectArray);



//            JSONObject json = new JSONObject();
//            json.put("enabled", true);
//            json.put("email", createUserDTO.getEmail());
//            json.put("firstName", createUserDTO.getFirstName());
//            json.put("lastName", createUserDTO.getLastName());
//            json.put("username", createUserDTO.getEmail());
//            json.put("credentials", objectArray);

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<Object> response = restTemplate.exchange(
                    "http://stadafint.se/admin/realms/cleanCode/users",
                    HttpMethod.POST,

                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            System.out.println(response);

            return response.toString();

//        } catch (HttpRequestFailedException e) {
//            throw new HttpRequestFailedException("Fetch failed");
//        }
    }
}
