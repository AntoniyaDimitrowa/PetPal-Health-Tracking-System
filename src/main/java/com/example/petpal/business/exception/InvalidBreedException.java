package com.example.petpal.business.exception;


public class InvalidBreedException extends Exception {

    public InvalidBreedException(long id) {
        super("Breed with id " + id + " does not exist.");
    }
}
