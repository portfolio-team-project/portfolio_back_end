package com.api.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.api.global.common.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                          .getFieldErrors()
                          .get(0)
                          .getDefaultMessage();
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(false, message, null));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraint(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                          .iterator().next()
                          .getMessage();
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(false, message, null));
    }
    
    // JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(false, "요청 형식이 올바르지 않습니다.", null));
    }
    
    // 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiResponse<>(false, "지원하지 않는 HTTP 메서드입니다.", null));
    }
    
    // 존재하지 않는 경로
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResource(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "요청한 리소스를 찾을 수 없습니다.", null));
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException e) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(false, e.getMessage(), null));
    }

    // 그 외 모든 예외 (안전망)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "서버 오류가 발생했습니다.", null));
    }
}