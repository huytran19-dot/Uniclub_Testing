package com.uniclub.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Không tìm thấy %s với %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
