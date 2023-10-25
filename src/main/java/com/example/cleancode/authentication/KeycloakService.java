package com.example.cleancode.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

            System.out.println(response);



            return response.toString();
        } catch (Exception e) {
            throw new Exception();
        }


    }



//    public String getAdminToken() {
//        String getTokenUrl = "http://stadafint.se/realms/master/protocol/openid-connect/token";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("grant_type", "password");
//        requestBody.put("username", "admin");
//        requestBody.put("password", "l?3t5!C1eAn\"tHäc0De.-");
//        requestBody.put("client_id", "admin-cli");
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody);
//
//        ResponseEntity<Map> response = restTemplate.exchange(getTokenUrl, HttpMethod.POST, request, Map.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            Map<String, Object> responseBody = response.getBody();
//            String accessToken = (String) responseBody.get("access_token");
//            return accessToken;
//        } else {
//            // Handle error response here
//            System.out.println("Error: " + response.getStatusCodeValue());
//            return "else"; // Or throw an exception, or handle the error differently
//        }
//    }
}
