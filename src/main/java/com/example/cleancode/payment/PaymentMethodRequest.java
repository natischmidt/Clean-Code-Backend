package com.example.cleancode.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethodRequest {

    private String cardNumber;
    private int expMonth;
    private int expYear;
    private int cvc;

}
