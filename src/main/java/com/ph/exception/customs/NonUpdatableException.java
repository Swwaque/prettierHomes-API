package com.ph.exception.customs;

public class NonUpdatableException extends RuntimeException {
    public NonUpdatableException(String message) {
        super(message);
    }

    public NonUpdatableException(String message, Throwable cause) {
        super(message, cause);
    }
}
