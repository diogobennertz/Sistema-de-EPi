package com.empresa.epi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "categoria_epi")
@Getter @Setter @NoArgsConstructor
public class CategoriaEpi {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    @NotBlank @Column(nullable = false, unique = true)
    private String nome;
}
