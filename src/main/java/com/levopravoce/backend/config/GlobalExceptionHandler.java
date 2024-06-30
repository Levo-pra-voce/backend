package com.levopravoce.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

      record ApiError(int status, String message) {}

      @ExceptionHandler({ Exception.class })
      public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
          String message = ex.getMessage();
          log.error("Handling exception: " + ex.getClass().getSimpleName() + " -> " + message);
          ApiError apiError = new ApiError(500, message.contains(":") ? message.split(":")[1].trim() : message);
          return ResponseEntity.status(apiError.status()).body(apiError);
      }
}
