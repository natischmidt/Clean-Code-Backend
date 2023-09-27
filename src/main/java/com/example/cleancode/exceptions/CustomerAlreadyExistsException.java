package com.example.cleancode.exceptions;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(String m){
        super(m);
    }
}
