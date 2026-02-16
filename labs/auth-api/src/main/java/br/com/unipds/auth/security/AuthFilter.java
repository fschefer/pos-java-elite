package br.com.unipds.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Passou pelo filtro na rota: " + request.getRequestURI());

        // Tenta decodificar o token da requisição
        Authentication auth = TokenUtil.decode(request);

        // Se o token for válido, avisa o Spring Security que a requisição está autenticada
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Passa a requisição para frente (para os próximos filtros ou para o Controller)
        filterChain.doFilter(request, response);
    }
}