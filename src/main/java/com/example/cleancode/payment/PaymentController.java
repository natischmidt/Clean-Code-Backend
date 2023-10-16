package com.example.cleancode.payment;

import com.stripe.model.Address;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.common.EmptyParam;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @PostMapping("/createCustomer")
    public ResponseEntity<String> createCustomer(@RequestParam String firstName,
                                                 @RequestParam String lastName,
                                                 @RequestParam String email) {
        try {
            Stripe.apiKey = "sk_test_51O0OipK1zCzSMEnJwG9lEhvL6kMcbIheCCEWTfdO4w0RFHWtxgFPiYdCef2nw4i4S4EhXRYBp0LhADeobqsKTPcI00vNGw8cuA";

//            Address addressObj = new Address.Builder()
//                    .setLine1(address)
//                    .build();

            CustomerCreateParams params = new CustomerCreateParams.Builder()
                    .setName(firstName + " " + lastName)
                    .setEmail(email)
//                    .setAddress(addressObj)
                    .build();

            Customer customer = Customer.create(params);

            return ResponseEntity.ok("Customer was created with the Stripe ID: " + customer.getId());
        } catch (StripeException e) {
            return new ResponseEntity<>("Failed to create new Customer in Stripe: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Funkar inte för man kan inte skicka in hårdkodad info till Stripe API (felkod 500)
    @PostMapping("/createPaymentMethod")
    public ResponseEntity<String> createPaymentMethod() {
        try {
            Stripe.apiKey = "sk_test_51O0OipK1zCzSMEnJwG9lEhvL6kMcbIheCCEWTfdO4w0RFHWtxgFPiYdCef2nw4i4S4EhXRYBp0LhADeobqsKTPcI00vNGw8cuA";

            Map<String, Object> card = new HashMap<>();
            card.put("number", "4242424242424242");
            card.put("exp_month", 8);
            card.put("exp_year", 2024);
            card.put("cvc", "314");
            Map<String, Object> params = new HashMap<>();
            params.put("type", "card");
            params.put("card", card);

            PaymentMethod paymentMethod = PaymentMethod.create(params);

            // Här kan du returnera en respons med information om den skapade betalningsmetoden
            return ResponseEntity.ok("Betalningsmetod skapad med Stripe ID: " + paymentMethod.getId());
        } catch (StripeException e) {
            return new ResponseEntity<>("Misslyckades med att skapa betalningsmetod i Stripe: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Behöver skicka med betalningsmetods id i Postman som ska genereras ovanför
    @PostMapping("/createPayment")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        try {
            Stripe.apiKey = "sk_test_51O0OipK1zCzSMEnJwG9lEhvL6kMcbIheCCEWTfdO4w0RFHWtxgFPiYdCef2nw4i4S4EhXRYBp0LhADeobqsKTPcI00vNGw8cuA";

            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setAmount(request.getAmount())
                    .setCurrency("sek")
                    .setCustomer(request.getEmail())
                    .setPaymentMethod(request.getPaymentMethod()) // Använd betalningsmetodens ID
                    .setDescription(request.getJobId().toString())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            return ResponseEntity.ok(paymentIntent.toJson());
        } catch (StripeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory();
    }

    @PostMapping("/payPayment")
    public ResponseEntity<String> payPayment(@RequestBody PayPaymentDTO request) {
        try {
            Stripe.apiKey = "sk_test_51O0OipK1zCzSMEnJwG9lEhvL6kMcbIheCCEWTfdO4w0RFHWtxgFPiYdCef2nw4i4S4EhXRYBp0LhADeobqsKTPcI00vNGw8cuA";

            PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentId());

            PaymentIntentConfirmParams confirmParams = new PaymentIntentConfirmParams.Builder()
                    .setPaymentMethod(request.getPaymentId())
                    .build();

            PaymentIntent confirmedIntent = paymentIntent.confirm(confirmParams);

            return ResponseEntity.ok(confirmedIntent.toJson());
        } catch (StripeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

