package com.example.cleancode.payment;

import com.stripe.param.PaymentIntentConfirmParams;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Controller
@RequestMapping("api/payment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @PostMapping("/createPayment")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        try {
            Stripe.apiKey = "sk_test_51O0OipK1zCzSMEnJwG9lEhvL6kMcbIheCCEWTfdO4w0RFHWtxgFPiYdCef2nw4i4S4EhXRYBp0LhADeobqsKTPcI00vNGw8cuA";

            PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                    .setAmount(request.getAmount())
                    .setCurrency("sek")
                    .setPaymentMethod(request.getPaymentMethod())
                    .setCustomer(request.getEmail())
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

