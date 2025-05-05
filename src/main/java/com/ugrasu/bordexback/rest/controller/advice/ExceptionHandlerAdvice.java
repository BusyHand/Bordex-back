package com.ugrasu.bordexback.rest.controller.advice;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError fieldError) {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    } else {
                        errors.put(error.getObjectName(), error.getDefaultMessage());
                    }
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();

        errorDetails.put("error", "Ошибка при чтении тела запроса. Проверьте формат JSON и переданные данные.");
        errorDetails.put("exception", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            errorDetails.put("causeException", cause.getClass().getSimpleName());
            errorDetails.put("causeMessage", cause.getMessage());

            if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
                com.fasterxml.jackson.databind.exc.InvalidFormatException ife = (com.fasterxml.jackson.databind.exc.InvalidFormatException) cause;
                errorDetails.put("invalidValue", ife.getValue());
                errorDetails.put("targetType", ife.getTargetType().getSimpleName());
                errorDetails.put("fieldPath", ife.getPath().stream()
                        .map(ref -> ref.getFieldName())
                        .collect(Collectors.joining(".")));
            }

            if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException) {
                com.fasterxml.jackson.databind.exc.MismatchedInputException mie = (com.fasterxml.jackson.databind.exc.MismatchedInputException) cause;
                errorDetails.put("targetType", mie.getTargetType() != null ? mie.getTargetType().getSimpleName() : null);
                errorDetails.put("fieldPath", mie.getPath().stream()
                        .map(ref -> ref.getFieldName())
                        .collect(Collectors.joining(".")));
            }

            if (cause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) {
                com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException upe = (com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) cause;
                errorDetails.put("unrecognizedField", upe.getPropertyName());
                errorDetails.put("knownFields", upe.getKnownPropertyIds());
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDetails);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityAlreadyExist(EntityExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}
