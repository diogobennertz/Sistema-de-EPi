package com.empresa.epi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
@Getter @Setter @NoArgsConstructor
public class Movimentacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TipoMovimentacao tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_epi")
    private Epi epi;

    // Entrega/Entrada/Devolução: positiva. Ajuste: diferença (pode ser negativa).
    @Column(nullable = false)
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "id_colaborador")
    private Colaborador colaborador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String referencia;

    @Column(length = 1000)
    private String observacao;

    public static Movimentacao de(TipoMovimentacao tipo, Epi epi, int quantidade, Colaborador colaborador,
                                  Usuario usuario, String referencia, String observacao, LocalDateTime quando) {
        Movimentacao m = new Movimentacao();
        m.dataHora = quando == null ? LocalDateTime.now() : quando;
        m.tipo = tipo;
        m.epi = epi;
        m.quantidade = quantidade;
        m.colaborador = colaborador;
        m.usuario = usuario;
        m.referencia = referencia;
        m.observacao = observacao;
        return m;
    }
}
