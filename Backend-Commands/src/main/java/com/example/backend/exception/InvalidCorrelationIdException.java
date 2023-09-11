package com.example.backend.exception;

public class InvalidCorrelationIdException extends RuntimeException {
    public InvalidCorrelationIdException(String message) {
        super(message);
    }
}
