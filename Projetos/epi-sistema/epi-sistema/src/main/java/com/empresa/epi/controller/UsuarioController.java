package com.empresa.epi.controller;

import com.empresa.epi.exception.RegraNegocioException;
import com.empresa.epi.model.Perfil;
import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Leitura: qualquer usuário logado. Criar/editar/inativar: só ADMIN (ver SecurityConfig).
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping public List<Usuario> listar() { return repo.findAll(Sort.by("id")); }

    @PostMapping
    public Usuario criar(@Valid @RequestBody Usuario u) {
        if (u.getSenha() == null || u.getSenha().length() < 6)
            throw new RegraNegocioException("A senha deve ter ao menos 6 caracteres.");
        u.setId(null);
        u.setDataCadastro(null);
        u.setSenha(encoder.encode(u.getSenha()));
        return repo.save(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario u) {
        return repo.findById(id).map(atual -> {
            boolean deixaDeSerAdminAtivo = atual.getPerfil() == Perfil.ADMIN && atual.isAtivo()
                    && (u.getPerfil() != Perfil.ADMIN || !u.isAtivo());
            garantirUmAdmin(deixaDeSerAdminAtivo);

            u.setId(id);
            u.setDataCadastro(atual.getDataCadastro());
            if (u.getSenha() == null || u.getSenha().isBlank()) {
                u.setSenha(atual.getSenha());               // em branco = mantém a senha
            } else if (u.getSenha().length() < 6) {
                throw new RegraNegocioException("A senha deve ter ao menos 6 caracteres.");
            } else {
                u.setSenha(encoder.encode(u.getSenha()));
            }
            return ResponseEntity.ok(repo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        return repo.findById(id).map(u -> {
            garantirUmAdmin(u.getPerfil() == Perfil.ADMIN && u.isAtivo());
            u.setAtivo(false);
            repo.save(u);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // Evita ficar sem nenhum administrador ativo (ninguém mais conseguiria gerenciar usuários)
    private void garantirUmAdmin(boolean operacaoRemoveAdmin) {
        if (operacaoRemoveAdmin && repo.countByPerfilAndAtivoTrue(Perfil.ADMIN) <= 1)
            throw new RegraNegocioException("Não é possível remover o último administrador ativo.");
    }
}
