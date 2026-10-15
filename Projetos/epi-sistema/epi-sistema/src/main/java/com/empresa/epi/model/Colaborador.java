package com.empresa.epi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "colaborador")
@Getter @Setter @NoArgsConstructor
public class Colaborador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long id;

    @NotBlank @Column(nullable = false)
    private String nome;

    @NotBlank @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @NotBlank @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    private String cargo;
    private String setor;
    private String telefone;
    private LocalDate dataAdmissao;
    private boolean ativo = true;
}
