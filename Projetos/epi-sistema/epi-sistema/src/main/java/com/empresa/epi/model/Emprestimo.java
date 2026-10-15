package com.empresa.epi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emprestimo")
@Getter @Setter @NoArgsConstructor
public class Emprestimo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHoraEntrega;

    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_colaborador")
    private Colaborador colaborador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonManagedReference
    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemEmprestimo> itens = new ArrayList<>();
}
