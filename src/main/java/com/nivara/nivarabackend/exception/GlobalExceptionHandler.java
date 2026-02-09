package com.nivara.nivarabackend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  DTO validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error_code", 400,
                        "message", message
                ));
    }

    //  Business logic errors (OTP wrong, user not found, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error_code", 400,
                        "message", ex.getMessage()
                ));
    }

    // DB unique constraint errors (duplicate email/mobile)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDb(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error_code", 400,
                        "message", "Duplicate data already exists"
                ));
    }

    //  fallback for unknown errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity.internalServerError()
                .body(Map.of(
                        "error_code", 500,
                        "message", "Internal server error"
                ));
    }
}
