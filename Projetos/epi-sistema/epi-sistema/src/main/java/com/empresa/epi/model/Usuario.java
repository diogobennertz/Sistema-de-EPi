package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank @Column(nullable = false)
    private String nome;

    @NotBlank @Email @Column(nullable = false, unique = true, length = 150)
    private String email;

    // Nunca devolvida pela API; guardada sempre com hash BCrypt
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;

    @NotNull @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Perfil perfil;

    private boolean ativo = true;

    private LocalDate dataCadastro;

    @PrePersist
    void antesDeSalvar() {
        if (dataCadastro == null) dataCadastro = LocalDate.now();
    }
}
