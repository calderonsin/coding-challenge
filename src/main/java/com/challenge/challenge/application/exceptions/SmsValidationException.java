package com.challenge.challenge.application.exceptions;

public class SmsValidationException extends RuntimeException {
    public SmsValidationException(String message) {
        super(message);
    }
}
