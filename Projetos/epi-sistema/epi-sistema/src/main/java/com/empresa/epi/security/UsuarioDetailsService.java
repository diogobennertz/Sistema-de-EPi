package com.empresa.epi.security;

import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;
    public UsuarioDetailsService(UsuarioRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario u = repo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(u.getLogin())
                .password(u.getSenha())
                .roles(u.getPerfil().name())
                .disabled(!u.isAtivo())
                .build();
    }
}
