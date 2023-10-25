package com.example.cleancode.authentication;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/auth/keycloak")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KeycloakController {

    private final KeycloakService keycloakService;

    public KeycloakController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/getAdminToken")
    public String getAdminToken() throws Exception {

        return keycloakService.getAdminToken();
    }

}
