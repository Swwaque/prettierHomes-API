package com.ph.exception.customs;

public class BuiltInFieldException extends RuntimeException {
    public BuiltInFieldException(String message) {
        super(message);
    }

    public BuiltInFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
