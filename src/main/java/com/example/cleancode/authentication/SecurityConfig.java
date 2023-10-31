package com.example.cleancode.authentication;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    public static final String ADMIN = "admin";
    public static final String EMPLOYEE = "employee";
    public static final String CUSTOMER = "customer";

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth ->
                {
                    auth.requestMatchers(HttpMethod.POST, "api/auth/loginCustomer/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/auth/logoutCustomer/").hasRole(CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "api/auth/loginEmployee").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "api/auth/logoutEmployee").hasAnyRole(ADMIN, EMPLOYEE);
                    auth.anyRequest().authenticated();
                });

        http
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
                ));

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();





//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> {
////                    auth.anyRequest().authenticated();
//                    auth.requestMatchers(HttpMethod.POST, "/api/customer/open/*").permitAll();
//                    auth.requestMatchers(HttpMethod.GET, "/api/customer/admin/*").hasRole(ADMIN);
//                    auth.requestMatchers(HttpMethod.GET, "/api/employee/*").hasRole(ADMIN);
//                    auth.requestMatchers(HttpMethod.POST, "/api/employee/*").hasRole(ADMIN);
//                    auth.requestMatchers(HttpMethod.POST, "/api/jobs/open/*").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
//                    auth.requestMatchers(HttpMethod.POST, "/api/jobs/admin/*").hasRole(ADMIN);
//                    auth.requestMatchers(HttpMethod.POST, "/api/jobs/employee/*").hasAnyRole(ADMIN, EMPLOYEE);
//
//                });

//        http.csrf(csrf -> csrf.disable());

//        http.authorizeHttpRequests(auth -> {
//            auth.requestMatchers(HttpMethod.GET, "/api/*").permitAll()
//                    .anyRequest().authenticated()
//            ;});


//        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(x -> x.jwtAuthenticationConverter(jwtAuthConverter)));
//
//        http.sessionManagement(session -> session.sessionCreationPolicy(
//                SessionCreationPolicy.STATELESS));

//        return http.build();
    }
}

