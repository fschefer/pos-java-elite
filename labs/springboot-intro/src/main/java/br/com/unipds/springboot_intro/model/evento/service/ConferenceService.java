package br.com.unipds.springboot_intro.model.evento.service;

import br.com.unipds.springboot_intro.model.evento.Conference;
import br.com.unipds.springboot_intro.model.evento.repository.ConferenceRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ConferenceService {
    private final ConferenceRepository repository;

    public ConferenceService(ConferenceRepository repository) {
        this.repository = repository;
    }

    public Conference create(Conference conference) {
        return repository.save(conference);
    }

    public Conference findById(Integer id) throws ChangeSetPersister.NotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public List<Conference> findAll() {
        return repository.findAll();
    }
}