package br.com.unipds.springboot_intro.evento;

import br.com.unipds.springboot_intro.model.evento.Session;
import br.com.unipds.springboot_intro.model.evento.Subscription;
import br.com.unipds.springboot_intro.model.evento.SubscriptionId;
import br.com.unipds.springboot_intro.model.evento.User;
import br.com.unipds.springboot_intro.model.evento.controller.SubscriptionController;
import br.com.unipds.springboot_intro.model.evento.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService service;

    @Test
    public void testCreateSubscription() throws Exception {
        // 1. Preparação do Cenário (Dados de Entrada)
        User user = new User();
        user.setId(1);

        Session session = new Session();
        session.setId(2);

        // Chave composta
        SubscriptionId id = new SubscriptionId(user, session);

        Subscription sub = new Subscription();
        sub.setId(id);
        sub.setLevel(1);

        // 2. Simulação do Serviço (Mock)
        // Quando o serviço for chamado, retorna o objeto com ID gerado simulado
        Subscription savedSub = new Subscription();
        savedSub.setId(id);
        savedSub.setLevel(1);
        savedSub.setUniqueId("12345-uuid-teste"); // Simula o ID gerado pelo serviço

        when(service.addSubscription(any(Subscription.class))).thenReturn(savedSub);

        // 3. Execução e Validação
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sub)))
                .andExpect(status().isCreated()) // Espera 201 Created
                .andExpect(jsonPath("$.uniqueId", is("12345-uuid-teste")))
                .andExpect(jsonPath("$.level", is(1)));
    }
}