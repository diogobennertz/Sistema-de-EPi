package com.empresa.epi.security;

import com.empresa.epi.model.Perfil;
import com.empresa.epi.model.Usuario;
import com.empresa.epi.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // Cria o primeiro administrador se o banco estiver sem usuários. TROQUE A SENHA depois!
    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@epi.local");
                admin.setSenha(encoder.encode("admin123"));
                admin.setPerfil(Perfil.ADMIN);
                repo.save(admin);
                System.out.println(">>> Usuário inicial criado: admin@epi.local / admin123");
            }
        };
    }
}
