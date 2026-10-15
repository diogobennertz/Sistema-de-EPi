package com.empresa.epi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "epi")
@Getter @Setter @NoArgsConstructor
public class Epi {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_epi")
    private Long id;

    @NotBlank @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotBlank @Column(nullable = false, length = 20)
    private String numeroCa;

    private LocalDate validadeCa;
    private String tamanho;

    @Min(0) @Column(nullable = false)
    private int quantidadeEstoque;

    private boolean ativo = true;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categoria")
    private CategoriaEpi categoria;
}
