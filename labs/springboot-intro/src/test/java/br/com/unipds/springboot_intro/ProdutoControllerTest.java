package br.com.unipds.springboot_intro;

import br.com.unipds.springboot_intro.model.Produto;
import br.com.unipds.springboot_intro.model.ProdutoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// --- IMPORTS ESTÁTICOS CORRIGIDOS ---
// Estes são os únicos necessários para os testes funcionarem
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRecuperarPorId() throws Exception {
        // Testa GET /produtos/1 (Computador)
        mockMvc.perform(get("/produtos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Computador")))
                .andExpect(jsonPath("$.preco", is(1500.0)));
    }

    @Test
    public void testAddNewProduct() throws Exception {
        // Testa POST /produtos
        Produto novoProduto = new Produto(101, "Teclado Mecânico", 250.00);

        String jsonBody = objectMapper.writeValueAsString(novoProduto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated()) // Espera 201 Created
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.nome", is("Teclado Mecânico")));
    }
}