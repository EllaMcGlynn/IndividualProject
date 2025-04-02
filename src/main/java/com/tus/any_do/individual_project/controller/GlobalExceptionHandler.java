package com.tus.any_do.individual_project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tus.any_do.individual_project.exception.InvalidCredentialsException;
import com.tus.any_do.individual_project.exception.ProjectNotFoundException;
import com.tus.any_do.individual_project.exception.TaskNotFoundException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToProjectException;
import com.tus.any_do.individual_project.exception.UnauthorizedAccessToTaskException;
import com.tus.any_do.individual_project.exception.UserAlreadyExistsException;
import com.tus.any_do.individual_project.exception.UserNotFoundException;

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
    
    // NOT FOUND EXCEPTIONS
    @ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    
    @ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    
    @ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
    
    
    // UNAUTHORIZED EXCEPTIONS
    @ExceptionHandler(UnauthorizedAccessToProjectException.class)
	public ResponseEntity<String> handleUnauthorizedAccessToProjectException(UnauthorizedAccessToProjectException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
    
    @ExceptionHandler(UnauthorizedAccessToTaskException.class)
	public ResponseEntity<String> handleUnauthorizedAccessToTaskException(UnauthorizedAccessToTaskException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}
    
}
