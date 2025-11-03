package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AvaliarDesafioUseCase {
    private static final Logger log = LoggerFactory.getLogger(AvaliarDesafioUseCase.class);
    private final DesafioRepositoryPort desafioRepository;

    public AvaliarDesafioUseCase(DesafioRepositoryPort desafioRepository) {
        this.desafioRepository = desafioRepository;
    }

    public Desafio executar(Long desafioId, Integer nota) {
        log.info("Iniciando avaliação do desafio ID: {} com nota: {}", desafioId, nota);
        
        if (nota == null || nota < 1 || nota > 5) {
            log.error("Nota inválida para desafio ID {}: {}", desafioId, nota);
            throw new ValidacaoException("Nota do desafio deve estar entre 1 e 5");
        }

        Desafio desafio = desafioRepository.buscarPorColaboradorId(desafioId)
            .stream()
            .filter(d -> d.getId().equals(desafioId))
            .findFirst()
            .orElseThrow(() -> {
                log.error("Desafio não encontrado: ID {}", desafioId);
                return new ValidacaoException("Desafio não encontrado");
            });

        desafio.setNota(nota);
        Desafio desafioAtualizado = desafioRepository.salvar(desafio);
        log.info("Desafio ID {} avaliado com sucesso. Nota: {}", desafioId, nota);
        return desafioAtualizado;
    }
}
