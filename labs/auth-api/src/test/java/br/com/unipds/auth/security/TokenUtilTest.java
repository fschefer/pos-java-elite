package br.com.unipds.auth.security;

import br.com.unipds.auth.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenUtilTest {

    @Test
    public void deveRetornarAuthenticationQuandoTokenForValido() {
        // Geramos um token criptografado verdadeiro
        User mockUser = new User();
        mockUser.setUsername("isidro");
        String tokenReal = TokenUtil.encode(mockUser).token();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenReal);

        // Executa
        Authentication auth = TokenUtil.decode(request);

        // Valida
        assertNotNull(auth, "A autenticação não deveria ser nula com um token real e válido");
        assertEquals("isidro", auth.getPrincipal());
    }

    @Test
    public void deveRetornarNullQuandoTokenForInvalido() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer string_falsa_sem_pontos");

        Authentication auth = TokenUtil.decode(request);

        assertNull(auth);
    }

    @Test
    public void deveRetornarNullQuandoNaoHouverCabecalhoAuthorization() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        Authentication auth = TokenUtil.decode(request);

        assertNull(auth);
    }

    @Test
    public void deveRetornarNullQuandoPrefixoBearerNaoEstiverPresente() {
        User mockUser = new User();
        mockUser.setUsername("isidro");
        String tokenReal = TokenUtil.encode(mockUser).token();

        HttpServletRequest request = mock(HttpServletRequest.class);
        // Enviou o token correto, mas esqueceu-se da palavra "Bearer "
        when(request.getHeader("Authorization")).thenReturn(tokenReal);

        Authentication auth = TokenUtil.decode(request);

        assertNull(auth);
    }
}