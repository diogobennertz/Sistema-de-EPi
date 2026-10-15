package com.empresa.epi.controller;

import com.empresa.epi.exception.RegraNegocioException;
import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Acesso restrito ao perfil ADMIN (ver SecurityConfig)
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping public List<Usuario> listar() { return repo.findAll(); }

    @PostMapping
    public Usuario criar(@Valid @RequestBody Usuario u) {
        if (u.getSenha() == null || u.getSenha().isBlank())
            throw new RegraNegocioException("Senha é obrigatória.");
        u.setId(null);
        u.setSenha(encoder.encode(u.getSenha()));
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario u) {
        return repo.findById(id).map(atual -> {
            u.setId(id);
            // Senha em branco = mantém a atual
            u.setSenha(u.getSenha() == null || u.getSenha().isBlank()
                    ? atual.getSenha() : encoder.encode(u.getSenha()));
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        return repo.findById(id).map(u -> { u.setAtivo(false); repo.save(u); return ResponseEntity.noContent().<Void>build(); })
                .orElse(ResponseEntity.notFound().build());
    }
}
