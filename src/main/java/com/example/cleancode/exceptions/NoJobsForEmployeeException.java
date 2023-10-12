package com.example.cleancode.exceptions;

public class NoJobsForEmployeeException extends RuntimeException{
    public NoJobsForEmployeeException(String message) {
        super(message);
    }
}
