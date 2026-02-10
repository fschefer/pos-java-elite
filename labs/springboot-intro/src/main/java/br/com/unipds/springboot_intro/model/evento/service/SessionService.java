package br.com.unipds.springboot_intro.model.evento.service;

import br.com.unipds.springboot_intro.model.evento.Conference;
import br.com.unipds.springboot_intro.model.evento.Session;
import br.com.unipds.springboot_intro.model.evento.repository.SessionRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository repository;
    private final ConferenceService conferenceService; // Dependência de outro serviço

    public SessionService(SessionRepository repository, ConferenceService conferenceService) {
        this.repository = repository;
        this.conferenceService = conferenceService;
    }

    public Session create(Session session, Integer conferenceId) throws ChangeSetPersister.NotFoundException {
        // Busca a conferência e associa à sessão antes de salvar
        Conference conference = conferenceService.findById(conferenceId);
        session.setConference(conference);
        return repository.save(session);
    }

    public List<Session> findAll() {
        return repository.findAll();
    }

    public Session findById(Integer id) throws ChangeSetPersister.NotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }
}