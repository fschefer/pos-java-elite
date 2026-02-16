package br.com.unipds.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Define o BCrypt como o codificador oficial de senhas da aplicação
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/open").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll() // Liberta a criação de utilizadores
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // Liberta o login
                .anyRequest().authenticated()
        );

        // Injeta o nosso filtro customizado
        http.addFilterBefore(new AuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}