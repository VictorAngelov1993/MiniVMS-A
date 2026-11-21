package com.minivms.paymentservice.exception;

import com.minivms.paymentservice.exception.custom.BudgetNotFoundException;
import com.minivms.paymentservice.exception.custom.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleDefaultException(Exception ex) {
        return this.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "Unexpected server error"
        );
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAmountException(InvalidAmountException exception) {
        return this.buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage() != null ? exception.getMessage() : "Bad Request"
        );
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBudgetNotFoundException(BudgetNotFoundException exception) {
        return this.buildErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage() != null ? exception.getMessage() : "Not Found"
        );
    }

}
