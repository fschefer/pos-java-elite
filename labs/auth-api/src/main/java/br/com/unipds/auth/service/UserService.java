package br.com.unipds.auth.service;

import br.com.unipds.auth.dto.MyToken;
import br.com.unipds.auth.model.User;
import br.com.unipds.auth.repository.UserRepository;
import br.com.unipds.auth.security.TokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User addUser(User user) {
        // Criptografa a senha em texto limpo antes de guardar na base de dados
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public MyToken login(User user) {
        // 1. Procura o utilizador
        User storedUser = repository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Compara a senha enviada em texto limpo com o Hash guardado no banco
        if (encoder.matches(user.getPassword(), storedUser.getPassword())) {
            // 3. Se a senha estiver correta, gera e devolve o JWT
            return TokenUtil.encode(storedUser);
        }

        // Se a senha estiver errada, lança exceção
        throw new RuntimeException("Unauthorized user (Invalid Password)");
    }
}