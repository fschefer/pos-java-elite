package br.com.unipds.springboot_intro.model.evento.controller;

import br.com.unipds.springboot_intro.model.evento.User;
import br.com.unipds.springboot_intro.model.evento.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(201).body(service.create(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(service.findById(id));
    }
}