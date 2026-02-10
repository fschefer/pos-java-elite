package br.com.unipds.springboot_intro.model.evento.repository;
import br.com.unipds.springboot_intro.model.evento.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    // Query Method: Busca automática pelo nome do atributo
    User findByEmail(String email);
}