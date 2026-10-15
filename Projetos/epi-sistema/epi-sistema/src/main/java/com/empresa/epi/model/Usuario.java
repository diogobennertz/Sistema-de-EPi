package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank @Column(nullable = false)
    private String nome;

    @NotBlank @Column(nullable = false, unique = true, length = 50)
    private String login;

    // Nunca devolvida nas respostas da API; guardada sempre com hash BCrypt
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String senha;

    @NotNull @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Perfil perfil;

    private boolean ativo = true;
}
