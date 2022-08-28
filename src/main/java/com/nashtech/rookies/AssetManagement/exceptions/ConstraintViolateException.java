package com.nashtech.rookies.AssetManagement.exceptions;

public class ConstraintViolateException extends RuntimeException{

    public ConstraintViolateException(String message) {
        super(message);
    }

    public ConstraintViolateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolateException(Throwable cause) {
        super(cause);
    }
}
