package com.ph.exception.customs;

public class RelatedFieldException extends RuntimeException {
    public RelatedFieldException(String message) {
        super(message);
    }

    public RelatedFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
