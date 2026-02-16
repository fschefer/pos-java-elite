package br.com.unipds.auth.service;

import br.com.unipds.auth.dto.MyToken;
import br.com.unipds.auth.model.User;
import br.com.unipds.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("isidro");
        testUser.setPassword("senha123");
    }

    @Test
    public void deveEncriptarASenhaECriarUtilizadorComSucesso() {
        // Simula o comportamento do BCrypt
        when(encoder.encode("senha123")).thenReturn("$2a$10$hashSimuladoAqui");
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User savedUser = service.addUser(testUser);

        assertNotNull(savedUser);
        assertEquals("$2a$10$hashSimuladoAqui", savedUser.getPassword());
        verify(encoder, times(1)).encode("senha123");
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void deveFazerLoginERetornarTokenQuandoCredenciaisForemValidas() {
        User storedUser = new User();
        storedUser.setUsername("isidro");
        storedUser.setPassword("$2a$10$hashSimuladoAqui");

        // Simula a base de dados a encontrar o utilizador
        when(repository.findByUsername("isidro")).thenReturn(Optional.of(storedUser));
        // Simula o BCrypt a confirmar que a senha limpa corresponde ao hash
        when(encoder.matches("senha123", "$2a$10$hashSimuladoAqui")).thenReturn(true);

        MyToken token = service.login(testUser);

        assertNotNull(token);
        assertNotNull(token.token()); // Garante que a string JWT foi gerada
    }

    @Test
    public void deveLancarExcecaoQuandoSenhaForInvalidaNoLogin() {
        User storedUser = new User();
        storedUser.setUsername("isidro");
        storedUser.setPassword("$2a$10$hashSimuladoAqui");

        when(repository.findByUsername("isidro")).thenReturn(Optional.of(storedUser));
        // Simula o BCrypt a rejeitar a senha
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.login(testUser);
        });

        assertEquals("Unauthorized user (Invalid Password)", exception.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoUtilizadorNaoExistirNoLogin() {
        when(repository.findByUsername("isidro")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.login(testUser);
        });

        assertEquals("User not found", exception.getMessage());
    }
}