package com.example.petpal.business.exception;

public class InvalidMoodException extends Exception {
    public InvalidMoodException(long id) {
        super("Mood with id " + id + " does not exist.");
    }
}
