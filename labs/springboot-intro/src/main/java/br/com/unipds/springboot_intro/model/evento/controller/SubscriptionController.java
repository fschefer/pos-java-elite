package br.com.unipds.springboot_intro.model.evento.controller;

import br.com.unipds.springboot_intro.model.evento.Subscription;
import br.com.unipds.springboot_intro.model.evento.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) {
        // O corpo da requisição deve conter o objeto aninhado 'id': { 'user': { 'id': 1 }, 'session': { 'id': 2 } }
        return ResponseEntity.status(201).body(service.addSubscription(subscription));
    }
}