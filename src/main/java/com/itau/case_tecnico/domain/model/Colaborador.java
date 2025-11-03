package com.itau.case_tecnico.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Colaborador {
    private Long id;
    private String matricula;
    private String nome;
    private String dataAdmissao;
    private String cargo;

    public Colaborador() {}

    public Colaborador(String matricula, String nome, String dataAdmissao, String cargo) {
        this.matricula = matricula;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
    }
}
