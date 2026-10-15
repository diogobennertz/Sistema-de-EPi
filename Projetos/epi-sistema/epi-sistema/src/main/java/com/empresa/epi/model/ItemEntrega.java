package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_entrega")
@Getter @Setter @NoArgsConstructor
public class ItemEntrega {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_entrega")
    private Long id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_entrega")
    private Entrega entrega;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_epi")
    private Epi epi;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private int quantidadeDevolvida = 0;

    // Condição da última devolução: Bom, Danificado ou Descartado
    private String condicao;
}
