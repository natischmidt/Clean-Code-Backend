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
    public static final String ADMIN = "client-admin";
    public static final String EMPLOYEE = "client-employee";
    public static final String CUSTOMER = "client-customer";

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable());

        http
                .authorizeHttpRequests(auth ->
                {
                    auth.requestMatchers(HttpMethod.POST,"/api/auth/loginCustomer").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/logoutCustomer/").hasRole(CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/loginEmployee").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/auth/logoutEmployee").hasAnyRole(ADMIN, EMPLOYEE);

                    auth.requestMatchers(HttpMethod.POST,"/api/customer/create").permitAll();
                    auth.requestMatchers(HttpMethod.DELETE,"/api/customer/delete/*").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.PATCH,"/api/customer/update/*").hasAnyRole(ADMIN, EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/customer/all").hasAnyRole(ADMIN,EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/customer/*").hasAnyRole(ADMIN,EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/customer/getByEmail/*").hasAnyRole(ADMIN, EMPLOYEE);

                    auth.requestMatchers(HttpMethod.POST,"/api/employee/createEmployee").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.DELETE,"/api/employee/deleteEmployee").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.GET,"/api/employee/getEmployee").hasAnyRole(ADMIN,EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/employee/getAllEmployees").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.PUT,"/api/employee/editEmployee").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.GET,"/api/employee/getSalary/*").hasAnyRole(ADMIN,EMPLOYEE);

                    auth.requestMatchers(HttpMethod.POST,"/api/jobs/createJob").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
                    auth.requestMatchers(HttpMethod.DELETE,"/api/jobs/deleteJob").hasRole(ADMIN);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getJob").hasAnyRole(ADMIN, EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getAllJobs").hasAnyRole(ADMIN);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getAllJobsForEmployee/*").hasAnyRole(ADMIN, EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getAllJobsForEmployeeWithStatus/*").hasAnyRole(ADMIN, EMPLOYEE);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getAllJobsForCustomer/*").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getAllJobsForCustomerWithStatus/*").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT,"/api/jobs/updateJob").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET,"/api/jobs/getByStatus").hasAnyRole(ADMIN, EMPLOYEE, CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST,"/api/jobs/getAvailableEmployees").hasAnyRole(ADMIN);






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

