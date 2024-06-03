package com.ph.exception.customs;

public class ValuesNotMatchException extends RuntimeException {
    public ValuesNotMatchException(String message) {
        super(message);
    }

    public ValuesNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
