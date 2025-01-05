package com.example.petpal.business.exception;


public class InvalidUserException extends Exception {

    public InvalidUserException(long id) {
        super("User with id " + id + " does not exist.");
    }
    public InvalidUserException(String message) {
        super(message);
    }
}
