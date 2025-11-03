package com.itau.case_tecnico.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"colaborador_id", "nota_final", "media_comportamental", "media_desafios"})
public class NotaFinalResponse {
    @JsonProperty("colaborador_id")
    private Long colaboradorId;
    
    @JsonProperty("nota_final")
    private Double notaFinal;
    
    @JsonProperty("media_comportamental")
    private Double mediaComportamental;
    
    @JsonProperty("media_desafios")
    private Double mediaDesafios;
}
