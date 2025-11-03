package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTodosUseCase {
    private static final Logger log = LoggerFactory.getLogger(ListarTodosUseCase.class);
    private final ColaboradorRepositoryPort colaboradorRepository;
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final DesafioRepositoryPort desafioRepository;

    public ListarTodosUseCase(ColaboradorRepositoryPort colaboradorRepository,
                              AvaliacaoRepositoryPort avaliacaoRepository,
                              DesafioRepositoryPort desafioRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.desafioRepository = desafioRepository;
    }

    public List<Colaborador> listarColaboradores() {
        log.info("Listando todos os colaboradores");
        List<Colaborador> colaboradores = colaboradorRepository.listarTodos();
        log.info("Total de colaboradores encontrados: {}", colaboradores.size());
        return colaboradores;
    }

    public List<AvaliacaoComportamental> listarAvaliacoes() {
        log.info("Listando todas as avaliações comportamentais");
        List<AvaliacaoComportamental> avaliacoes = avaliacaoRepository.listarTodas();
        log.info("Total de avaliações encontradas: {}", avaliacoes.size());
        return avaliacoes;
    }

    public List<Desafio> listarDesafios() {
        log.info("Listando todos os desafios");
        List<Desafio> desafios = desafioRepository.listarTodos();
        log.info("Total de desafios encontrados: {}", desafios.size());
        return desafios;
    }
}
