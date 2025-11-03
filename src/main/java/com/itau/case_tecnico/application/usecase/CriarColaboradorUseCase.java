package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CriarColaboradorUseCase {
    private static final Logger log = LoggerFactory.getLogger(CriarColaboradorUseCase.class);
    private final ColaboradorRepositoryPort colaboradorRepository;

    public CriarColaboradorUseCase(ColaboradorRepositoryPort colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public Colaborador executar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: matrícula {}", colaborador.getMatricula());
        
        if (colaboradorRepository.existePorMatricula(colaborador.getMatricula())) {
            log.error("Tentativa de cadastrar matrícula já existente: {}", colaborador.getMatricula());
            throw new ValidacaoException("Matrícula já cadastrada: " + colaborador.getMatricula());
        }
        
        if (colaborador.getMatricula() == null || colaborador.getMatricula().isBlank()) {
            log.error("Tentativa de criar colaborador sem matrícula");
            throw new ValidacaoException("Matrícula é obrigatória");
        }
        
        if (colaborador.getNome() == null || colaborador.getNome().isBlank()) {
            log.error("Tentativa de criar colaborador sem nome");
            throw new ValidacaoException("Nome é obrigatório");
        }
        
        Colaborador colaboradorSalvo = colaboradorRepository.salvar(colaborador);
        log.info("Colaborador criado com sucesso: ID {}, matrícula {}", colaboradorSalvo.getId(), colaboradorSalvo.getMatricula());
        return colaboradorSalvo;
    }
}
