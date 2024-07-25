package com.example.task.web.controller;

import com.example.task.domain.exception.AccessDeniedException;
import com.example.task.domain.exception.ExceptionBody;
import com.example.task.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourcedNotFound(ResourceNotFoundException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalState(IllegalStateException e) {
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class,
            org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDenied() {
        return new ExceptionBody("Access denied");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //Эти ограничения могут включать уникальность, неравенство null, связь с другими таблицами
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingMessage, newMessage) ->
                                existingMessage + " " + newMessage)
                ));
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerConstraintViolation(ConstraintViolationException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        return exceptionBody;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerAuthentication(AuthenticationException e) {
        return new ExceptionBody("Authentication failed: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerException(Exception e) {
        e.printStackTrace();
        return new ExceptionBody("Internal error: " + e.getMessage());
    }

}
