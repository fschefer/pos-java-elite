package br.com.unipds.springboot_intro.model.evento.service;

import br.com.unipds.springboot_intro.model.evento.Session;
import br.com.unipds.springboot_intro.model.evento.Subscription;
import br.com.unipds.springboot_intro.model.evento.User;
import br.com.unipds.springboot_intro.model.evento.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public Subscription addSubscription(Subscription subscription) {
        // Regra de Negócio: Preencher dados de auditoria e controle automaticamente
        subscription.setCreatedAt(LocalDateTime.now());

        // Gera um código único para o bilhete/inscrição
        if (subscription.getUniqueId() == null || subscription.getUniqueId().isEmpty()) {
            subscription.setUniqueId(UUID.randomUUID().toString());
        }

        // Define um nível padrão se não for informado (ex: 1 - Básico)
        if (subscription.getLevel() == null) {
            subscription.setLevel(1);
        }

        return repository.save(subscription);
    }

    public List<Subscription> findAll() {
        return repository.findAll();
    }

    public List<Subscription> findByUser(User user) {
        return repository.findByIdUser(user);
    }

    public List<Subscription> findBySession(Session session) {
        return repository.findByIdSession(session);
    }
}