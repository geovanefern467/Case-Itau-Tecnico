package com.itau.case_tecnico.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoComportamental {
    private Long id;
    private Long colaboradorId;
    private String tipo;
    private Integer nota;

    public AvaliacaoComportamental() {}

    public AvaliacaoComportamental(Long colaboradorId, String tipo, Integer nota) {
        this.colaboradorId = colaboradorId;
        this.tipo = tipo;
        this.nota = nota;
    }
}
