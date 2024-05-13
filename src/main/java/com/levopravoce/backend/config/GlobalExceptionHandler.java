package com.levopravoce.backend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

      record ApiError(int status, String message) {}

      @ExceptionHandler({ Exception.class })
      public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
          ApiError apiError = new ApiError(500, ex.getMessage());
          return ResponseEntity.status(apiError.status()).body(apiError);
      }
}
