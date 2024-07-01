package com.levopravoce.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({Exception.class})
  public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
    String message = ex.getMessage();
    message = message.contains("Exception:") ? message.split("Exception:")[1].trim() : message;
    log.error("Handling exception: " + ex.getClass().getSimpleName() + " -> " + message);
    if (ex instanceof ResponseStatusException exception) {
      HttpStatusCode statusCode = exception.getStatusCode();
      return ResponseEntity.status(statusCode).body(new ApiError(statusCode.value(), message));
    }
    ApiError apiError = new ApiError(500, message);
    return ResponseEntity.status(apiError.status()).body(apiError);
  }

  record ApiError(int status, String message) {

  }
}
