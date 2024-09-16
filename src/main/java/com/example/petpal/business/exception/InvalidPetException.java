package com.example.petpal.business.exception;


public class InvalidPetException extends Exception {

    public InvalidPetException(Long id) {
        super("Pet with id " + id + " does not exist.");
    }
}
