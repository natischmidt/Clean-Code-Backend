package com.example.cleancode.exceptions;

public class CustomerDoesNotExistException extends RuntimeException{
    public CustomerDoesNotExistException(Long id){
        super("Customer with ID: " + id + " does not exist.");
    }
}
