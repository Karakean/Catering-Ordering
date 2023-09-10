package com.catering.commons.exception;

public class CateringNotFoundException extends RuntimeException {
    public CateringNotFoundException(String message) {
        super(message);
    }
}
