package com.example.petpal.business.exception;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
        super("Credentials are incorrect!");
    }
}
