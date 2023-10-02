//package com.example.cleancode.mail;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//
//@SpringBootApplication
//public class SpringEmailApplication {
//
//    @Autowired
//    private EmailService emailService;
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringEmailApplication.class, args);
//
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() {
//        emailService.sendEmail("stadafintab@gmail.com",
//                "StädaFint AB",
//                "Du är nu registrerad som medlem på StädaFint AB. Välkommen!");
//    }
//
//}
