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

import java.util.Optional;

@Service
public class BuscarPorIdUseCase {
    private static final Logger log = LoggerFactory.getLogger(BuscarPorIdUseCase.class);
    private final ColaboradorRepositoryPort colaboradorRepository;
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final DesafioRepositoryPort desafioRepository;

    public BuscarPorIdUseCase(ColaboradorRepositoryPort colaboradorRepository,
                              AvaliacaoRepositoryPort avaliacaoRepository,
                              DesafioRepositoryPort desafioRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.desafioRepository = desafioRepository;
    }

    public Optional<Colaborador> buscarColaborador(Long id) {
        log.info("Buscando colaborador por ID: {}", id);
        Optional<Colaborador> colaborador = colaboradorRepository.buscarPorId(id);
        log.debug("Colaborador ID {}: {}", id, colaborador.isPresent() ? "encontrado" : "não encontrado");
        return colaborador;
    }

    public Optional<AvaliacaoComportamental> buscarAvaliacao(Long id) {
        log.info("Buscando avaliação por ID: {}", id);
        Optional<AvaliacaoComportamental> avaliacao = avaliacaoRepository.buscarPorId(id);
        log.debug("Avaliação ID {}: {}", id, avaliacao.isPresent() ? "encontrada" : "não encontrada");
        return avaliacao;
    }

    public Optional<Desafio> buscarDesafio(Long id) {
        log.info("Buscando desafio por ID: {}", id);
        Optional<Desafio> desafio = desafioRepository.buscarPorId(id);
        log.debug("Desafio ID {}: {}", id, desafio.isPresent() ? "encontrado" : "não encontrado");
        return desafio;
    }
}
