package com.itau.case_tecnico.domain.port;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepositoryPort {
    AvaliacaoComportamental salvar(AvaliacaoComportamental avaliacao);
    List<AvaliacaoComportamental> buscarPorColaboradorId(Long colaboradorId);
    List<AvaliacaoComportamental> listarTodas();
    Optional<AvaliacaoComportamental> buscarPorId(Long id);
}
