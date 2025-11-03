package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AvaliarComportamentoUseCase {
    private static final Logger log = LoggerFactory.getLogger(AvaliarComportamentoUseCase.class);
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final ColaboradorRepositoryPort colaboradorRepository;

    private static final List<String> TIPOS_VALIDOS = Arrays.asList(
        "Você promove um ambiente colaborativo?",
        "Você se atualiza e aprende o tempo todo?",
        "Você utiliza dados para tomar decisões?",
        "Você trabalha com autonomia?"
    );

    public AvaliarComportamentoUseCase(AvaliacaoRepositoryPort avaliacaoRepository,
                                       ColaboradorRepositoryPort colaboradorRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public AvaliacaoComportamental executar(AvaliacaoComportamental avaliacao) {
        log.info("Iniciando avaliação comportamental para colaborador ID: {}, tipo: {}", 
                 avaliacao.getColaboradorId(), avaliacao.getTipo());
        
        if (!colaboradorRepository.buscarPorId(avaliacao.getColaboradorId()).isPresent()) {
            log.error("Colaborador não encontrado: ID {}", avaliacao.getColaboradorId());
            throw new ValidacaoException("Colaborador não encontrado");
        }

        if (avaliacao.getTipo() == null || avaliacao.getTipo().isBlank()) {
            log.error("Tentativa de criar avaliação sem tipo");
            throw new ValidacaoException("Tipo de avaliação é obrigatório");
        }

        if (!TIPOS_VALIDOS.contains(avaliacao.getTipo())) {
            log.error("Tipo de avaliação inválido: {}", avaliacao.getTipo());
            throw new ValidacaoException("Tipo de avaliação inválido. Tipos válidos: " + String.join(", ", TIPOS_VALIDOS));
        }

        validarNota(avaliacao.getNota());
        
        List<AvaliacaoComportamental> avaliacoesExistentes = 
            avaliacaoRepository.buscarPorColaboradorId(avaliacao.getColaboradorId());
        
        log.debug("Colaborador ID {} possui {} avaliações comportamentais", 
                  avaliacao.getColaboradorId(), avaliacoesExistentes.size());
        
        if (avaliacoesExistentes.size() >= 4) {
            log.error("Colaborador ID {} já possui 4 avaliações comportamentais", avaliacao.getColaboradorId());
            throw new ValidacaoException("Colaborador já possui 4 avaliações comportamentais");
        }

        boolean tipoJaExiste = avaliacoesExistentes.stream()
            .anyMatch(a -> a.getTipo().equals(avaliacao.getTipo()));
        
        if (tipoJaExiste) {
            log.error("Colaborador ID {} já possui avaliação do tipo: {}", 
                      avaliacao.getColaboradorId(), avaliacao.getTipo());
            throw new ValidacaoException("Já existe avaliação do tipo: " + avaliacao.getTipo());
        }

        AvaliacaoComportamental avaliacaoSalva = avaliacaoRepository.salvar(avaliacao);
        log.info("Avaliação comportamental criada com sucesso: ID {}, Colaborador ID {}, Tipo: {}", 
                 avaliacaoSalva.getId(), avaliacao.getColaboradorId(), avaliacao.getTipo());
        return avaliacaoSalva;
    }

    private void validarNota(Integer nota) {
        if (nota == null || nota < 1 || nota > 5) {
            log.error("Nota inválida: {}", nota);
            throw new ValidacaoException("Nota deve estar entre 1 e 5");
        }
    }
}
