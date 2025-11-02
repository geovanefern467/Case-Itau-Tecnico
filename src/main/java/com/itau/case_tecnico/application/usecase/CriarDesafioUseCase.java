package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CriarDesafioUseCase {
    private static final Logger log = LoggerFactory.getLogger(CriarDesafioUseCase.class);
    private final DesafioRepositoryPort desafioRepository;
    private final ColaboradorRepositoryPort colaboradorRepository;

    public CriarDesafioUseCase(DesafioRepositoryPort desafioRepository,
                               ColaboradorRepositoryPort colaboradorRepository) {
        this.desafioRepository = desafioRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public Desafio executar(Desafio desafio) {
        log.info("Iniciando criação de desafio para colaborador ID: {}", desafio.getColaboradorId());
        
        if (!colaboradorRepository.buscarPorId(desafio.getColaboradorId()).isPresent()) {
            log.error("Colaborador não encontrado: ID {}", desafio.getColaboradorId());
            throw new ValidacaoException("Colaborador não encontrado");
        }

        int quantidadeDesafios = desafioRepository.contarPorColaboradorId(desafio.getColaboradorId());
        log.debug("Colaborador possui {} desafios cadastrados", quantidadeDesafios);
        
        if (quantidadeDesafios >= 4) {
            log.error("Colaborador ID {} já possui {} desafios (máximo: 4)", desafio.getColaboradorId(), quantidadeDesafios);
            throw new ValidacaoException("Colaborador já possui 4 desafios (máximo permitido)");
        }

        if (desafio.getDescricao() == null || desafio.getDescricao().isBlank()) {
            log.error("Tentativa de criar desafio sem descrição");
            throw new ValidacaoException("Descrição do desafio é obrigatória");
        }

        validarNota(desafio.getNota());

        Desafio desafioSalvo = desafioRepository.salvar(desafio);
        log.info("Desafio criado com sucesso: ID {}, Colaborador ID {}", desafioSalvo.getId(), desafio.getColaboradorId());
        return desafioSalvo;
    }

    private void validarNota(Integer nota) {
        if (nota == null || nota < 1 || nota > 5) {
            log.error("Nota inválida: {}", nota);
            throw new ValidacaoException("Nota deve estar entre 1 e 5");
        }
    }
}
