package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesafioRequest {
    @JsonProperty("colaborador_id")
    private Long colaboradorId;
    
    private String descricao;
    
    private Integer nota;
    
    @JsonProperty("pontuacao")
    public void setPontuacao(Integer pontuacao) { 
        this.nota = pontuacao; 
    }
}
