package com.empresa.epi.controller;

import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository repo;
    public AuthController(UsuarioRepository repo) { this.repo = repo; }

    // Usado pela tela de login: se responder 200, as credenciais são válidas
    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        Usuario u = repo.findByEmail(auth.getName()).orElseThrow();
        return Map.of("id", u.getId(), "nome", u.getNome(), "email", u.getEmail(),
                "perfil", u.getPerfil().name());
    }
}
