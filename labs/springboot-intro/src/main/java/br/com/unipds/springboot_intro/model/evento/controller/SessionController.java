package br.com.unipds.springboot_intro.model.evento.controller;

import br.com.unipds.springboot_intro.model.evento.Session;
import br.com.unipds.springboot_intro.model.evento.service.SessionService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class SessionController {
    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    // Exemplo: POST /sessions?conferenceId=1
    @PostMapping
    public ResponseEntity<Session> create(@RequestBody Session session, @RequestParam Integer conferenceId) throws ChangeSetPersister.NotFoundException {
        Session created = service.create(session, conferenceId);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}