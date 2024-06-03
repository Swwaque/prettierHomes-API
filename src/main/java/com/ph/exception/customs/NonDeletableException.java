package com.ph.exception.customs;

public class NonDeletableException extends RuntimeException {

    public NonDeletableException(String message) {
        super(message);
    }

    public NonDeletableException(String message, Throwable cause) {
        super(message, cause);
    }
}
