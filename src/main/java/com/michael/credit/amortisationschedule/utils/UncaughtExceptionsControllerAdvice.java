package com.michael.credit.amortisationschedule.utils;

import com.michael.credit.amortisationschedule.exception.FieldErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice(annotations = {RestController.class})
public class UncaughtExceptionsControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, BindException.class})
    public ResponseEntity<Object> exception(BindException exception) {
        Map<String, String> errors = new HashMap<>();
        System.out.println(exception.toString());
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .badRequest()
                .body(FieldErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value()).errors(errors).build());
    }
}