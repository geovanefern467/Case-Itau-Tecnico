package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AvaliacaoComportamentalRequest {
    @JsonProperty("colaborador_id")
    private Long colaboradorId;
    private String tipo;
    private Integer nota;
}
