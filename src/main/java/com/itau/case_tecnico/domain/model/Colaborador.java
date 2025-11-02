package com.itau.case_tecnico.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Colaborador {
    private Long id;
    private String matricula;
    private String nome;
    private String dataAdmissao;
    private String cargo;
    private List<AvaliacaoComportamental> avaliacoes;
    private List<Desafio> desafios;

    public Colaborador() {
        this.avaliacoes = new ArrayList<>();
        this.desafios = new ArrayList<>();
    }

    public Colaborador(String matricula, String nome, String dataAdmissao, String cargo) {
        this();
        this.matricula = matricula;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
    }
}
