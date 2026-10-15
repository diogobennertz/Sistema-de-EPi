package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "item_emprestimo")
@Getter @Setter @NoArgsConstructor
public class ItemEmprestimo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_emprestimo")
    private Emprestimo emprestimo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_epi")
    private Epi epi;

    @Column(nullable = false)
    private int quantidade;

    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusItem status = StatusItem.PENDENTE;
}
