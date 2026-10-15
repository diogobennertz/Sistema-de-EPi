package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entrega")
@Getter @Setter @NoArgsConstructor
public class Entrega {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Long id;

    @Column(nullable = false)
    private LocalDate dataEntrega;

    private LocalDate dataPrevistaDevolucao;

    @Column(length = 1000)
    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_colaborador")
    private Colaborador colaborador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonManagedReference
    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemEntrega> itens = new ArrayList<>();
}
