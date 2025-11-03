package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.infrastructure.adapter.entity.AvaliacaoComportamentalEntity;
import com.itau.case_tecnico.infrastructure.adapter.entity.ColaboradorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringDataAvaliacaoComportamentalRepositoryTest {

    @Mock
    private AvaliacaoJpaRepository jpaRepository;

    @Mock
    private ColaboradorJpaRepository jpaColaboradorRepository;

    @InjectMocks
    private AvaliacaoRepositoryAdapter repository;

    private AvaliacaoComportamental avaliacao;
    private AvaliacaoComportamentalEntity entity;
    private ColaboradorEntity colaboradorEntity;

    @BeforeEach
    void setUp() {
        colaboradorEntity = new ColaboradorEntity();
        colaboradorEntity.setId(1L);
        colaboradorEntity.setMatricula("12345");
        colaboradorEntity.setNome("João Silva");
        colaboradorEntity.setDataAdmissao("2024-01-15");
        colaboradorEntity.setCargo("Dev");

        avaliacao = new AvaliacaoComportamental();
        avaliacao.setColaboradorId(1L);
        avaliacao.setTipo("Ambiente colaborativo");
        avaliacao.setNota(5);

        entity = new AvaliacaoComportamentalEntity();
        entity.setId(1L);
        entity.setColaborador(colaboradorEntity);
        entity.setTipo("Ambiente colaborativo");
        entity.setNota(5);
    }

    @Test
    void deveSalvarAvaliacaoComSucesso() {
        when(jpaColaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorEntity));
        when(jpaRepository.save(any(AvaliacaoComportamentalEntity.class))).thenReturn(entity);

        AvaliacaoComportamental resultado = repository.salvar(avaliacao);

        assertNotNull(resultado);
        assertEquals("Ambiente colaborativo", resultado.getTipo());
        assertEquals(5, resultado.getNota());
        verify(jpaRepository, times(1)).save(any(AvaliacaoComportamentalEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExiste() {
        when(jpaColaboradorRepository.findById(999L)).thenReturn(Optional.empty());
        
        avaliacao.setColaboradorId(999L);

        assertThrows(RuntimeException.class, () -> repository.salvar(avaliacao));
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void deveBuscarPorColaboradorIdComSucesso() {
        when(jpaRepository.findByColaboradorId(1L)).thenReturn(Arrays.asList(entity));

        List<AvaliacaoComportamental> resultado = repository.buscarPorColaboradorId(1L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Ambiente colaborativo", resultado.get(0).getTipo());
        verify(jpaRepository, times(1)).findByColaboradorId(1L);
    }

    @Test
    void deveRetornarListaVaziaQuandoAvaliacaoNaoExiste() {
        when(jpaRepository.findByColaboradorId(999L)).thenReturn(Arrays.asList());

        List<AvaliacaoComportamental> resultado = repository.buscarPorColaboradorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodasAvaliacoes() {
        AvaliacaoComportamentalEntity entity2 = new AvaliacaoComportamentalEntity();
        entity2.setId(2L);
        entity2.setColaborador(colaboradorEntity);
        entity2.setTipo("Aprende o tempo todo");
        entity2.setNota(4);

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity, entity2));

        List<AvaliacaoComportamental> resultado = repository.listarTodas();

        assertEquals(2, resultado.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<AvaliacaoComportamental> resultado = repository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Ambiente colaborativo", resultado.get().getTipo());
        verify(jpaRepository, times(1)).findById(1L);
    }
}
