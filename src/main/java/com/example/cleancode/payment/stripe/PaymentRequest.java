package com.example.cleancode.payment.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {

    private Long amount;
    private String paymentMethod;
    private String email;
    private Long jobId;

}
