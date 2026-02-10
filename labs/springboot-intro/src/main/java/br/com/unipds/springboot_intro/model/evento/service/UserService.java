package br.com.unipds.springboot_intro.model.evento.service;

import br.com.unipds.springboot_intro.model.evento.User;
import br.com.unipds.springboot_intro.model.evento.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        // Regra de negócio: Poderia verificar duplicidade de email aqui
        return repository.save(user);
    }

    public User findById(Integer id) throws ChangeSetPersister.NotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}