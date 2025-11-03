package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import com.itau.case_tecnico.infrastructure.adapter.entity.ColaboradorEntity;
import com.itau.case_tecnico.infrastructure.adapter.entity.DesafioEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface DesafioJpaRepository extends JpaRepository<DesafioEntity, Long> {
    List<DesafioEntity> findByColaboradorId(Long colaboradorId);
    int countByColaboradorId(Long colaboradorId);
}

@Component
class DesafioRepositoryAdapter implements DesafioRepositoryPort {
    private static final Logger log = LoggerFactory.getLogger(DesafioRepositoryAdapter.class);
    private final DesafioJpaRepository jpaRepository;
    private final ColaboradorJpaRepository colaboradorJpaRepository;

    public DesafioRepositoryAdapter(DesafioJpaRepository jpaRepository,
                                    ColaboradorJpaRepository colaboradorJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.colaboradorJpaRepository = colaboradorJpaRepository;
    }

    @Override
    public Desafio salvar(Desafio desafio) {
        log.debug("Salvando desafio no banco: colaborador ID {}", desafio.getColaboradorId());
        DesafioEntity entity = toEntity(desafio);
        DesafioEntity saved = jpaRepository.save(entity);
        log.debug("Desafio salvo com sucesso no banco: ID {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    public List<Desafio> buscarPorColaboradorId(Long colaboradorId) {
        log.debug("Buscando desafios no banco para colaborador ID: {}", colaboradorId);
        List<Desafio> desafios = jpaRepository.findByColaboradorId(colaboradorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
        log.debug("Total de desafios encontrados para colaborador ID {}: {}", colaboradorId, desafios.size());
        return desafios;
    }

    @Override
    public int contarPorColaboradorId(Long colaboradorId) {
        log.debug("Contando desafios no banco para colaborador ID: {}", colaboradorId);
        int count = jpaRepository.countByColaboradorId(colaboradorId);
        log.debug("Total de desafios para colaborador ID {}: {}", colaboradorId, count);
        return count;
    }

    @Override
    public Optional<Desafio> buscarPorId(Long id) {
        log.debug("Buscando desafio no banco por ID: {}", id);
        Optional<Desafio> result = jpaRepository.findById(id)
            .map(this::toDomain);
        log.debug("Resultado da busca de desafio ID {}: {}", id, result.isPresent() ? "encontrado" : "não encontrado");
        return result;
    }

    @Override
    public List<Desafio> listarTodos() {
        log.debug("Listando todos os desafios do banco de dados");
        List<Desafio> desafios = jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
        log.debug("Total de desafios recuperados do banco: {}", desafios.size());
        return desafios;
    }

    private DesafioEntity toEntity(Desafio desafio) {
        DesafioEntity entity = new DesafioEntity();
        entity.setId(desafio.getId());
        
        ColaboradorEntity colaborador = colaboradorJpaRepository.findById(desafio.getColaboradorId())
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        entity.setColaborador(colaborador);
        
        entity.setDescricao(desafio.getDescricao());
        entity.setNota(desafio.getNota());
        return entity;
    }

    private Desafio toDomain(DesafioEntity entity) {
        Desafio desafio = new Desafio();
        desafio.setId(entity.getId());
        desafio.setColaboradorId(entity.getColaborador().getId());
        desafio.setDescricao(entity.getDescricao());
        desafio.setNota(entity.getNota());
        return desafio;
    }
}
