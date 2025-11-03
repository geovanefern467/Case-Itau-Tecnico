package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColaboradorRequest {
    private String matricula;
    private String nome;
    
    @JsonProperty("data_admissao")
    private String dataAdmissao;    
    private String cargo;
}
