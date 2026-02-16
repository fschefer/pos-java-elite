package br.com.unipds.auth.security;

import br.com.unipds.auth.controller.SimpleController;
import br.com.unipds.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimpleController.class)
@Import(WebSecurityConfig.class)
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void devePermitirAcessoNaRotaOpenSemToken() throws Exception {
        mockMvc.perform(get("/open"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World from OPEN endpoint! (Sem token)"));
    }

    @Test
    public void deveBloquearAcessoNaRotaRestrictedSemToken() throws Exception {
        mockMvc.perform(get("/restricted"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void devePermitirAcessoNaRotaRestrictedComTokenValido() throws Exception {
        // GERAR UM TOKEN REAL VÁLIDO PARA O TESTE
        User mockUser = new User();
        mockUser.setUsername("admin_teste");
        String tokenReal = TokenUtil.encode(mockUser).token();

        // Passamos o token real no cabeçalho
        mockMvc.perform(get("/restricted")
                        .header("Authorization", "Bearer " + tokenReal))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo à área SECRETA! (Token validado)"));
    }

    @Test
    public void deveBloquearAcessoNaRotaRestrictedComTokenInvalido() throws Exception {
        // Enviar um token mal formatado ou falso
        mockMvc.perform(get("/restricted")
                        .header("Authorization", "Bearer HackerToken999"))
                .andExpect(status().isForbidden());
    }
}