package com.ph.exception.customs;

public class JsonValidationException extends RuntimeException {

    public JsonValidationException(String message) {
        super(message);
    }

    public JsonValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
