package com.itau.case_tecnico.infrastructure.adapter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "colaborador")
public class ColaboradorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colaborador_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_admissao", nullable = false, length = 50)
    private String dataAdmissao;

    @Column(nullable = false, length = 50)
    private String cargo;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvaliacaoComportamentalEntity> avaliacoes = new ArrayList<>();

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DesafioEntity> desafios = new ArrayList<>();
}
