package br.com.unipds.springboot_intro.model.evento.repository;

import br.com.unipds.springboot_intro.model.evento.Conference;
import br.com.unipds.springboot_intro.model.evento.Session;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface SessionRepository extends ListCrudRepository<Session, Integer> {
    // Busca todas as sessões de uma conferência específica
    List<Session> findByConference(Conference conference);
}
