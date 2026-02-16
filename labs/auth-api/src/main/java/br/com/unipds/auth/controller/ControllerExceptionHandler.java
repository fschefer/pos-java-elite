package br.com.unipds.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    // Se o login falhar, o serviço lança RuntimeException e este método apanha-a
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        // Retorna 403 Forbidden com a mensagem de erro
        return ResponseEntity.status(403).body(Map.of("error", ex.getMessage()));
    }
}