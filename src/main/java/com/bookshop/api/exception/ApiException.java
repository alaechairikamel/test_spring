package com.bookshop.api.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
