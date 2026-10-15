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

    @NotBlank @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @NotBlank @Column(nullable = false)
    private String nome;

    @NotBlank @Column(nullable = false)
    private String categoria;

    @NotBlank @Column(nullable = false)
    private String fabricante;

    @NotBlank @Column(nullable = false, length = 20)
    private String numeroCa;

    @NotNull
    private LocalDate validadeCa;

    // Validade física do equipamento (opcional)
    private LocalDate validadeFisica;

    @Min(0) @Column(nullable = false)
    private int estoqueMinimo;

    // Só muda por entrada, ajuste, entrega e devolução (nunca pelo cadastro)
    @Column(nullable = false)
    private int quantidadeEstoque;

    // true = reutilizável (gera devolução); false = consumível
    private boolean reutilizavel = true;

    @Column(length = 1000)
    private String descricao;

    private boolean ativo = true;
}
