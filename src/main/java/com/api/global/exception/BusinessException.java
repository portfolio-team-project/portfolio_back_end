package com.api.global.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
