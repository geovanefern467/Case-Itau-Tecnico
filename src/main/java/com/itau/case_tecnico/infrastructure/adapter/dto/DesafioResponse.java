package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"desafio_id", "colaborador_id", "matricula", "nome", "cargo", "descricao", "nota"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesafioResponse {
    @JsonProperty("desafio_id")
    private Long id;
    
    @JsonProperty("colaborador_id")
    private Long colaboradorId;
    
    private String matricula;
    private String nome;
    private String cargo;
    
    private String descricao;
    private Integer nota;
}
