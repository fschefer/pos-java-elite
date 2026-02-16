package br.com.unipds.auth.security;

import br.com.unipds.auth.dto.MyToken;
import br.com.unipds.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {

    private static final String ISSUER = "professor_isidro";
    private static final long EXPIRATION = 15 * 1000; // 15 segundos (para testes)

    // A chave secreta deve ter pelo menos 32 caracteres (256 bits) para o HMAC-SHA256
    private static final String SECRET_STRING = "0123456789012345678901234567890123456789";

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    }

    // Método para GERAR o token após o login com sucesso
    public static MyToken encode(User user) {
        String jwtToken = Jwts.builder()
                .subject(user.getUsername())
                .issuer(ISSUER)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();

        return new MyToken(jwtToken);
    }

    // Método para DECODIFICAR e VALIDAR o token a cada requisição
    public static Authentication decode(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");

            try {
                // O parser verifica automaticamente a assinatura e a expiração do token
                Claims claims = Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String subject = claims.getSubject();
                String issuer = claims.getIssuer();
                Date expiration = claims.getExpiration();

                // Verifica se os dados batem e se ainda não expirou
                if (subject != null && ISSUER.equals(issuer) && expiration.after(new Date())) {
                    return new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                }
            } catch (Exception e) {
                // Se der erro (assinatura inválida, expirado, etc), retorna null e bloqueia o acesso
                System.out.println("Token inválido ou expirado: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}