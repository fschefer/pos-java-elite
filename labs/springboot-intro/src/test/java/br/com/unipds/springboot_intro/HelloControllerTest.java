package br.com.unipds.springboot_intro;

import br.com.unipds.springboot_intro.model.Produto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SpringbootIntroApplication.class)
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSayHello() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Spring Boot!"));
    }

    @Test
    public void testExibirProduto() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(9876)))
                .andExpect(jsonPath("$.nome", is("Computador")))
                .andExpect(jsonPath("$.preco", is(1500.0)));
    }

    @Test
    public void testAddNewProduct() throws Exception {
        Produto novoProduto = new Produto(101, "Teclado Mecânico", 250.00);

        String jsonBody = objectMapper.writeValueAsString(novoProduto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}