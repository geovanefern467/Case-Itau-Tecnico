package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"colaborador_id", "matricula", "nome", "data_admissao", "cargo"})
public class ColaboradorResponse {
    @JsonProperty("colaborador_id")
    private Long id;
    private String matricula;
    private String nome;
    
    @JsonProperty("data_admissao") 
    private String dataAdmissao;   
    
    private String cargo;
}
