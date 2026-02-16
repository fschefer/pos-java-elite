package com.unipds.bank.controller;

import com.unipds.bank.exception.InvalidAccountException;
import com.unipds.bank.exception.InvalidBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<Object> handleInvalidAccount(InvalidAccountException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage())); // 404 Not Found
    }

    @ExceptionHandler(InvalidBalanceException.class)
    public ResponseEntity<Object> handleInvalidBalance(InvalidBalanceException ex) {
        return ResponseEntity.status(400).body(Map.of("error", ex.getMessage())); // 400 Bad Request
    }
}