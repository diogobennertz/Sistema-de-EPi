package com.empresa.epi.security;

import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repo;
    public UsuarioDetailsService(UsuarioRepository repo) { this.repo = repo; }

    // O "username" do login é o e-mail
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(u.getEmail())
                .password(u.getSenha())
                .roles(u.getPerfil().name())
                .disabled(!u.isAtivo())
                .build();
    }
}
