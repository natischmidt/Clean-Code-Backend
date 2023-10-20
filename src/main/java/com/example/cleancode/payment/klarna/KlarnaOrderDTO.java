package com.example.cleancode.payment.klarna;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/*@Data
public class KlarnaOrderDTO {

    private String purchase_country = "SE";
    private String purchase_currency = "SEK";
    private String locale = "sv-SE";
    private int order_amount = 10000;
    private int order_tax_amount = 2500;

    @Data
    public static class OrderLine {
        private String type = "service";
        private String reference = "42-666-SE";
        private String name = "Basic Cleaning";
        private int quantity = 1;
        private String quantity_unit = "pcs";
        private int unit_price = 10000;
        private int tax_rate = 2500;
        private int total_amount = 10000;
        private int total_discount_amount = 0;
        private int total_tax_amount = 2500;
     }
    @JsonProperty("merchant_urls")
    private MerchantUrls merchantUrls;

    @Data
    public static class MerchantUrls {
        private String terms = "https://www.example.com/terms.html";
        private String checkout = "https://www.example.com/checkout.html?order_id={checkout.order.id}";
        private String confirmation = "https://www.example.com/confirmation.html?order_id={checkout.order.id}";
        private String push = "https://www.example.com/api/push?order_id={checkout.order.id}";
    }
}*/
@Data
public class KlarnaOrderDTO {

    @JsonProperty("purchase_country")
    private String purchase_country;

    @JsonProperty("purchase_currency")
    private String purchase_currency;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("order_amount")
    private int order_amount;

    @JsonProperty("order_tax_amount")
    private int order_tax_amount;

    @JsonProperty("order_lines")
    private List<OrderLine> orderLines;

    @Data
    public static class OrderLine {
        @JsonProperty("name")
        private String name;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("unit_price")
        private int unit_price;

        @JsonProperty("tax_rate")
        private int tax_rate;

        @JsonProperty("total_amount")
        private int total_amount;

        @JsonProperty("total_discount_amount")
        private int total_discount_amount;

        @JsonProperty("total_tax_amount")
        private int total_tax_amount;
    }

    @JsonProperty("merchant_urls")
    private MerchantUrls merchantUrls;

    @Data
    public static class MerchantUrls {
        @JsonProperty("terms")
        private String terms;

        @JsonProperty("checkout")
        private String checkout;

        @JsonProperty("confirmation")
        private String confirmation;

        @JsonProperty("push")
        private String push;
    }
}

