package com.example.exception;

public class ReferentialIntegrityException extends RuntimeException {

    public ReferentialIntegrityException(String message) {
        super(message);
    }

    public ReferentialIntegrityException(String resource, String linkedResource) {
        super(String.format("Cannot delete %s because it has linked %s", resource, linkedResource));
    }
}
