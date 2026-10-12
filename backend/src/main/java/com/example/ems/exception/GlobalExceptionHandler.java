package com.example.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiExceptions.NotFound.class)
    ResponseEntity<?> notFound(ApiExceptions.NotFound ex) { return response(HttpStatus.NOT_FOUND, ex.getMessage()); }
    @ExceptionHandler(ApiExceptions.BadRequest.class)
    ResponseEntity<?> badRequest(ApiExceptions.BadRequest ex) { return response(HttpStatus.BAD_REQUEST, ex.getMessage()); }
    @ExceptionHandler(ApiExceptions.Unauthorized.class)
    ResponseEntity<?> unauthorized(ApiExceptions.Unauthorized ex) { return response(HttpStatus.UNAUTHORIZED, ex.getMessage()); }
    @ExceptionHandler(ApiExceptions.Forbidden.class)
    ResponseEntity<?> forbidden(ApiExceptions.Forbidden ex) { return response(HttpStatus.FORBIDDEN, ex.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) errors.put(error.getField(), error.getDefaultMessage());
        return ResponseEntity.badRequest().body(Map.of("timestamp", LocalDateTime.now(), "message", "Validation failed", "errors", errors));
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<?> other(Exception ex) { return response(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"); }
    private ResponseEntity<?> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("timestamp", LocalDateTime.now(), "message", message));
    }
}
