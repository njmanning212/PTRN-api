package com.njman.ptrnapi.exceptions;

public class AuthorizationDeniedException extends RuntimeException {
    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
