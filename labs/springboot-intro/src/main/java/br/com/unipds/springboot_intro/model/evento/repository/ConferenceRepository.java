package br.com.unipds.springboot_intro.model.evento.repository;

import br.com.unipds.springboot_intro.model.evento.Conference;
import org.springframework.data.repository.ListCrudRepository;

public interface ConferenceRepository extends ListCrudRepository<Conference, Integer> {
}