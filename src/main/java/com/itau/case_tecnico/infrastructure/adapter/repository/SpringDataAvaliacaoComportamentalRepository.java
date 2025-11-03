package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.infrastructure.adapter.entity.AvaliacaoComportamentalEntity;
import com.itau.case_tecnico.infrastructure.adapter.entity.ColaboradorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface AvaliacaoJpaRepository extends JpaRepository<AvaliacaoComportamentalEntity, Long> {
    List<AvaliacaoComportamentalEntity> findByColaboradorId(Long colaboradorId);
}

@Component
class AvaliacaoRepositoryAdapter implements AvaliacaoRepositoryPort {
    private static final Logger log = LoggerFactory.getLogger(AvaliacaoRepositoryAdapter.class);
    private final AvaliacaoJpaRepository jpaRepository;
    private final ColaboradorJpaRepository colaboradorJpaRepository;

    public AvaliacaoRepositoryAdapter(AvaliacaoJpaRepository jpaRepository,
                                      ColaboradorJpaRepository colaboradorJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.colaboradorJpaRepository = colaboradorJpaRepository;
    }

    @Override
    public AvaliacaoComportamental salvar(AvaliacaoComportamental avaliacao) {
        log.debug("Salvando avaliação comportamental no banco: colaborador ID {}, tipo {}", 
                  avaliacao.getColaboradorId(), avaliacao.getTipo());
        AvaliacaoComportamentalEntity entity = toEntity(avaliacao);
        AvaliacaoComportamentalEntity saved = jpaRepository.save(entity);
        log.debug("Avaliação comportamental salva com sucesso no banco: ID {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    public List<AvaliacaoComportamental> buscarPorColaboradorId(Long colaboradorId) {
        log.debug("Buscando avaliações comportamentais no banco para colaborador ID: {}", colaboradorId);
        List<AvaliacaoComportamental> avaliacoes = jpaRepository.findByColaboradorId(colaboradorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
        log.debug("Total de avaliações encontradas para colaborador ID {}: {}", colaboradorId, avaliacoes.size());
        return avaliacoes;
    }

    @Override
    public List<AvaliacaoComportamental> listarTodas() {
        log.debug("Listando todas as avaliações comportamentais do banco de dados");
        List<AvaliacaoComportamental> avaliacoes = jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
        log.debug("Total de avaliações recuperadas do banco: {}", avaliacoes.size());
        return avaliacoes;
    }

    @Override
    public Optional<AvaliacaoComportamental> buscarPorId(Long id) {
        log.debug("Buscando avaliação comportamental no banco por ID: {}", id);
        Optional<AvaliacaoComportamental> result = jpaRepository.findById(id)
            .map(this::toDomain);
        log.debug("Resultado da busca de avaliação ID {}: {}", id, result.isPresent() ? "encontrada" : "não encontrada");
        return result;
    }

    private AvaliacaoComportamentalEntity toEntity(AvaliacaoComportamental avaliacao) {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        if (avaliacao.getId() != null) {
            entity.setId(avaliacao.getId());
        }
        
        ColaboradorEntity colaborador = colaboradorJpaRepository.findById(avaliacao.getColaboradorId())
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        entity.setColaborador(colaborador);
        
        entity.setTipo(avaliacao.getTipo());
        entity.setNota(avaliacao.getNota());
        return entity;
    }

    private AvaliacaoComportamental toDomain(AvaliacaoComportamentalEntity entity) {
        AvaliacaoComportamental avaliacao = new AvaliacaoComportamental();
        avaliacao.setId(entity.getId());
        avaliacao.setColaboradorId(entity.getColaborador().getId());
        avaliacao.setTipo(entity.getTipo());
        avaliacao.setNota(entity.getNota());
        return avaliacao;
    }
}