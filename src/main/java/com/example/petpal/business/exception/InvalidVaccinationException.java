package com.example.petpal.business.exception;

public class InvalidVaccinationException extends Exception {
    public InvalidVaccinationException(long id) {
        super("Vaccination with id " + id + " does not exist.");
    }

}
