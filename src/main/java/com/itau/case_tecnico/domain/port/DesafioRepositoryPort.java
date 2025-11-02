package com.itau.case_tecnico.domain.port;

import com.itau.case_tecnico.domain.model.Desafio;
import java.util.List;
import java.util.Optional;

public interface DesafioRepositoryPort {
    Desafio salvar(Desafio desafio);
    List<Desafio> buscarPorColaboradorId(Long colaboradorId);
    int contarPorColaboradorId(Long colaboradorId);
    List<Desafio> listarTodos();
    Optional<Desafio> buscarPorId(Long id);
}
