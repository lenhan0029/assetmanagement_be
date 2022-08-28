package com.nashtech.rookies.AssetManagement.exceptions;

public class ApiDeniedException extends RuntimeException{

    public ApiDeniedException(String message) {
        super(message);
    }

    public ApiDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
