package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.infrastructure.adapter.entity.ColaboradorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface ColaboradorJpaRepository extends JpaRepository<ColaboradorEntity, Long> {
    Optional<ColaboradorEntity> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
}

@Component
class SpringColaboradorRepositoryAdapter implements ColaboradorRepositoryPort {
    private static final Logger log = LoggerFactory.getLogger(SpringColaboradorRepositoryAdapter.class);
    private final ColaboradorJpaRepository jpaRepository;

    public SpringColaboradorRepositoryAdapter(ColaboradorJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Colaborador salvar(Colaborador colaborador) {
        log.debug("Salvando colaborador no banco de dados: matrícula {}", colaborador.getMatricula());
        ColaboradorEntity entity = toEntity(colaborador);
        ColaboradorEntity saved = jpaRepository.save(entity);
        log.debug("Colaborador salvo com sucesso no banco: ID {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    public Optional<Colaborador> buscarPorId(Long id) {
        log.debug("Buscando colaborador no banco por ID: {}", id);
        Optional<Colaborador> result = jpaRepository.findById(id).map(this::toDomain);
        log.debug("Resultado da busca de colaborador ID {}: {}", id, result.isPresent() ? "encontrado" : "não encontrado");
        return result;
    }

    @Override
    public Optional<Colaborador> buscarPorMatricula(String matricula) {
        log.debug("Buscando colaborador no banco por matrícula: {}", matricula);
        Optional<Colaborador> result = jpaRepository.findByMatricula(matricula).map(this::toDomain);
        log.debug("Resultado da busca de colaborador por matrícula {}: {}", matricula, result.isPresent() ? "encontrado" : "não encontrado");
        return result;
    }

    @Override
    public boolean existePorMatricula(String matricula) {
        log.debug("Verificando existência de matrícula no banco: {}", matricula);
        boolean exists = jpaRepository.existsByMatricula(matricula);
        log.debug("Matrícula {} existe: {}", matricula, exists);
        return exists;
    }

    @Override
    public List<Colaborador> listarTodos() {
        log.debug("Listando todos os colaboradores do banco de dados");
        List<Colaborador> colaboradores = jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
        log.debug("Total de colaboradores recuperados do banco: {}", colaboradores.size());
        return colaboradores;
    }

    private ColaboradorEntity toEntity(Colaborador colaborador) {
        ColaboradorEntity entity = new ColaboradorEntity();
        entity.setId(colaborador.getId());
        entity.setMatricula(colaborador.getMatricula());
        entity.setNome(colaborador.getNome());
        entity.setDataAdmissao(colaborador.getDataAdmissao());
        entity.setCargo(colaborador.getCargo());
        return entity;
    }

    private Colaborador toDomain(ColaboradorEntity entity) {
        Colaborador colaborador = new Colaborador();
        colaborador.setId(entity.getId());
        colaborador.setMatricula(entity.getMatricula());
        colaborador.setNome(entity.getNome());
        colaborador.setDataAdmissao(entity.getDataAdmissao());
        colaborador.setCargo(entity.getCargo());
        return colaborador;
    }
}
