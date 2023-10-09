package com.levo_pra_voce.backend.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    record ErrorResponse(String message) {}

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
