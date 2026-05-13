package com.api.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.global.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntime(RuntimeException e) {
        return new ApiResponse<>(false, e.getMessage(), null);
    }
}
