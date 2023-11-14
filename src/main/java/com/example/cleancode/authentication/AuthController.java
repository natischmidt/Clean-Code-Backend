package com.example.cleancode.authentication;

import com.example.cleancode.customer.CustomerAuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;
    private final KeycloakService keycloakService;

    public AuthController(AuthService authService, KeycloakService keycloakService) {
        this.authService = authService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/loginCustomer")
    public CustomerAuthenticationResponseDTO loginCustomer(@RequestBody AuthDTO authDTO) {
        return authService.loginCustomer(authDTO);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @PostMapping("/logoutCustomer")
    public void logoutCustomer(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @PostMapping("/loginEmployee")
    public AuthResponseDTO loginEmployee(@RequestBody AuthDTO authDTO) {
        return authService.loginEmployee(authDTO);
    }

    @PostMapping("/logoutEmployee")
    public void logoutEmployee(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @GetMapping("/logout/{id}")
    public String logout(@PathVariable String id) {

        return authService.logout(id);

    }
}
