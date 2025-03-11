package com.tus.anyDo.IndividualProject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tus.anyDo.IndividualProject.exception.InvalidCredentialsException;
import com.tus.anyDo.IndividualProject.exception.UserAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> jkla(UserAlreadyExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleValidationException(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
