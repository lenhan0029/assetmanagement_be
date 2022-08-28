package com.nashtech.rookies.AssetManagement.exceptions;

public class ResourceAlreadyExistException extends RuntimeException{

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
