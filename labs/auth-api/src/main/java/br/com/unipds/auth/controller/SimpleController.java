package br.com.unipds.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/open")
    public ResponseEntity<String> openEndpoint() {
        return ResponseEntity.ok("Hello World from OPEN endpoint! (Sem token)");
    }

    @GetMapping("/restricted")
    public ResponseEntity<String> restrictedEndpoint() {
        return ResponseEntity.ok("Bem-vindo à área SECRETA! (Token validado)");
    }
}