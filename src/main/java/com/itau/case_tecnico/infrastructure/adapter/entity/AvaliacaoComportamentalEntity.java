package com.itau.case_tecnico.infrastructure.adapter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avaliacao_comportamental")
public class AvaliacaoComportamentalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avaliacao_comportamental_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private ColaboradorEntity colaborador;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(nullable = false)
    private Integer nota;
}
