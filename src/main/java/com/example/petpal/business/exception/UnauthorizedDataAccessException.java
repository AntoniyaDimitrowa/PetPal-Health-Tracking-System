package com.example.petpal.business.exception;

public class UnauthorizedDataAccessException extends Exception {
    public UnauthorizedDataAccessException() {
        super("The user is not authorized to access this data!");
    }
}
