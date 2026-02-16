package br.com.unipds.auth.controller;

import br.com.unipds.auth.dto.MyToken;
import br.com.unipds.auth.model.User;
import br.com.unipds.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.status(201).body(service.addUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<MyToken> login(@RequestBody User user) {
        return ResponseEntity.ok(service.login(user));
    }
}