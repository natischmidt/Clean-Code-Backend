package com.example.cleancode.exceptions;

public class CustomerInfoMissmatchException extends RuntimeException{
    public CustomerInfoMissmatchException(String m){
        super(m);
    }
}
