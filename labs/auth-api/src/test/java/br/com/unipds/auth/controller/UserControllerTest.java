package br.com.unipds.auth.controller;

import br.com.unipds.auth.dto.MyToken;
import br.com.unipds.auth.model.User;
import br.com.unipds.auth.security.WebSecurityConfig;
import br.com.unipds.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
// Importa a nossa configuração de segurança para liberar as rotas públicas nos testes
@Import(WebSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @Test
    public void deveCriarUtilizadorERetornarStatus201() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("12345");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("admin");
        savedUser.setPassword("$2a$10$criptografia...");

        when(service.addUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.password").value("$2a$10$criptografia..."));
    }

    @Test
    public void deveFazerLoginERetornarStatus200ComToken() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("12345");

        MyToken tokenResponse = new MyToken("eyJhbGciOiJIUzI1NiJ9...");

        when(service.login(any(User.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.token").value("eyJhbGciOiJIUzI1NiJ9..."));
    }

    @Test
    public void deveRetornarStatus403QuandoLoginFalhar() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("senha_errada");

        // Simula a exceção lançada pelo serviço
        when(service.login(any(User.class)))
                .thenThrow(new RuntimeException("Unauthorized user (Invalid Password)"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden()) // 403 Forbidden interceptado pelo @ControllerAdvice
                .andExpect(jsonPath("$.error").value("Unauthorized user (Invalid Password)"));
    }
}