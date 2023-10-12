package com.example.cleancode.authentication;

import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/loginCustomer")
    public UUID loginCustomer(@RequestBody AuthDTO authDTO) {
        return authService.loginCustomer(authDTO);
    }

    @PostMapping("/loginEmployee")
    public AuthResponseDTO loginEmployee(@RequestBody AuthDTO authDTO) {
        return authService.loginEmployee(authDTO);
    }
}
