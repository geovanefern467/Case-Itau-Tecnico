package com.itau.case_tecnico.domain.port;

import com.itau.case_tecnico.domain.model.Colaborador;
import java.util.List;
import java.util.Optional;

public interface ColaboradorRepositoryPort {
    Optional<Colaborador> buscarPorId(Long id);
    Colaborador salvar(Colaborador colaborador);
    List<Colaborador> listarTodos();
    Optional<Colaborador> buscarPorMatricula(String matricula);
    boolean existePorMatricula(String matricula);
}
