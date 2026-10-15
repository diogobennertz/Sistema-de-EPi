package com.empresa.epi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                // Leitura: qualquer perfil logado (inclui CONSULTA)
                .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                // Gestão de usuários: só ADMIN
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                // Demais alterações: ADMIN e GESTOR
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "GESTOR")
                // Arquivos estáticos (o front)
                .anyRequest().permitAll())
            // 401 sem WWW-Authenticate: o navegador não abre o pop-up de login
            .httpBasic(b -> b.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }
}
