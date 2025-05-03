package com.coding.car_rental_app.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {

    private ResponseEntity<Object> errorResponseBuilder(String message, HttpStatus status){
        return new ResponseEntity<>(new ErrorResponse(
                status,
                message,
                LocalDateTime.now()),
                status);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex){
        return errorResponseBuilder(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> validationExceptionHandler(ValidationException ex){
        return errorResponseBuilder(ex.getMessage(), HttpStatus.NOT_FOUND);

    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ProcessingImgException.class)
    public ResponseEntity<Object> errorProcessingImgExceptionHandler(ValidationException ex){
        return errorResponseBuilder(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentException(MethodArgumentNotValidException ex){
           Map<String, String> errors = new HashMap<>();
           ex.getBindingResult().getFieldErrors().forEach(error-> {
               errors.put(error.getField(), error.getDefaultMessage());
           });

           return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //for handling repeated updates exception(throw exception when it's already updated)
    @org.springframework.web.bind.annotation.ExceptionHandler(RecurrentUpdateException.class)
    public ResponseEntity<Object> handleRecurrentUpdateException(RecurrentUpdateException ex){
        return errorResponseBuilder(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MinimumDurationInDaysException.class)
    public ResponseEntity<Object> handleMinimumDurationInDaysException(MinimumDurationInDaysException ex){
        return errorResponseBuilder(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    //to handle access denied globally(not authorized users)
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex){

           return errorResponseBuilder("You are not authorized to access this resource!", HttpStatus.FORBIDDEN);
    }
}
