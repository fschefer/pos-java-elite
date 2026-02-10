package br.com.unipds.springboot_intro.model.evento.repository;

import br.com.unipds.springboot_intro.model.evento.Session;
import br.com.unipds.springboot_intro.model.evento.Subscription;
import br.com.unipds.springboot_intro.model.evento.SubscriptionId;
import br.com.unipds.springboot_intro.model.evento.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SubscriptionRepository extends ListCrudRepository<Subscription, SubscriptionId> {
    // Acesso à propriedade composta 'id.user'
    List<Subscription> findByIdUser(User user);

    // Acesso à propriedade composta 'id.session'
    List<Subscription> findByIdSession(Session session);
}