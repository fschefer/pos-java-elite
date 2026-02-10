package br.com.unipds.springboot_intro.model.evento.controller;

import br.com.unipds.springboot_intro.model.evento.Conference;
import br.com.unipds.springboot_intro.model.evento.service.ConferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
    private final ConferenceService service;

    public ConferenceController(ConferenceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Conference> create(@RequestBody Conference conference) {
        return ResponseEntity.status(201).body(service.create(conference));
    }

    @GetMapping
    public ResponseEntity<List<Conference>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}