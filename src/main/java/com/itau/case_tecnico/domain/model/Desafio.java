package com.itau.case_tecnico.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Desafio {
    private Long id;
    private Long colaboradorId;
    private String descricao;
    private Integer nota;

    public Desafio() {}

    public Desafio(Long colaboradorId, String descricao, Integer nota) {
        this.colaboradorId = colaboradorId;
        this.descricao = descricao;
        this.nota = nota;
    }
}
