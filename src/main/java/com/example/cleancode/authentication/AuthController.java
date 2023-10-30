package com.example.cleancode.authentication;

import com.example.cleancode.customer.CustomerAuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
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
    public CustomerAuthenticationResponseDTO loginCustomer(@RequestBody AuthDTO authDTO) {
        return authService.loginCustomer(authDTO);
    }

//    @PreAuthorize("hasRole('customer')")
    @PostMapping("/logoutCustomer")
    public void logoutCustomer(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @PostMapping("/loginEmployee")
    public AuthResponseDTO loginEmployee(@RequestBody AuthDTO authDTO) {
        return authService.loginEmployee(authDTO);
    }

//    @PreAuthorize("hasAnyRole('admin', 'employee')")
    @PostMapping("/logoutEmployee")
    public void logoutEmployee(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
