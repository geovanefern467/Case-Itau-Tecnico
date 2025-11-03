package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.infrastructure.adapter.entity.ColaboradorEntity;
import com.itau.case_tecnico.infrastructure.adapter.entity.DesafioEntity;
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
class SpringDataDesafioRepositoryTest {

    @Mock
    private DesafioJpaRepository jpaRepository;

    @Mock
    private ColaboradorJpaRepository jpaColaboradorRepository;

    @InjectMocks
    private DesafioRepositoryAdapter repository;

    private Desafio desafio;
    private DesafioEntity entity;
    private ColaboradorEntity colaboradorEntity;

    @BeforeEach
    void setUp() {
        colaboradorEntity = new ColaboradorEntity();
        colaboradorEntity.setId(1L);
        colaboradorEntity.setMatricula("12345");
        colaboradorEntity.setNome("João Silva");
        colaboradorEntity.setDataAdmissao("2024-01-15");
        colaboradorEntity.setCargo("Dev");

        desafio = new Desafio();
        desafio.setColaboradorId(1L);
        desafio.setDescricao("Implementar API REST");
        desafio.setNota(85);

        entity = new DesafioEntity();
        entity.setId(1L);
        entity.setColaborador(colaboradorEntity);
        entity.setDescricao("Implementar API REST");
        entity.setNota(85);
    }

    @Test
    void deveSalvarDesafioComSucesso() {
        when(jpaColaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorEntity));
        when(jpaRepository.save(any(DesafioEntity.class))).thenReturn(entity);

        Desafio resultado = repository.salvar(desafio);

        assertNotNull(resultado);
        assertEquals("Implementar API REST", resultado.getDescricao());
        assertEquals(85, resultado.getNota());
        verify(jpaRepository, times(1)).save(any(DesafioEntity.class));
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExiste() {
        when(jpaColaboradorRepository.findById(999L)).thenReturn(Optional.empty());
        
        desafio.setColaboradorId(999L);

        assertThrows(RuntimeException.class, () -> repository.salvar(desafio));
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void deveListarDesafiosPorColaboradorId() {
        DesafioEntity entity2 = new DesafioEntity();
        entity2.setId(2L);
        entity2.setColaborador(colaboradorEntity);
        entity2.setDescricao("Outro desafio");
        entity2.setNota(90);

        when(jpaRepository.findByColaboradorId(1L)).thenReturn(Arrays.asList(entity, entity2));

        List<Desafio> resultado = repository.buscarPorColaboradorId(1L);

        assertEquals(2, resultado.size());
        assertEquals("Implementar API REST", resultado.get(0).getDescricao());
        assertEquals("Outro desafio", resultado.get(1).getDescricao());
        verify(jpaRepository, times(1)).findByColaboradorId(1L);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaDesafios() {
        when(jpaRepository.findByColaboradorId(999L)).thenReturn(Arrays.asList());

        List<Desafio> resultado = repository.buscarPorColaboradorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveContarDesafiosPorColaborador() {
        when(jpaRepository.countByColaboradorId(1L)).thenReturn(3);

        int count = repository.contarPorColaboradorId(1L);

        assertEquals(3, count);
        verify(jpaRepository, times(1)).countByColaboradorId(1L);
    }

    @Test
    void deveListarTodosDesafios() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity));

        List<Desafio> resultado = repository.listarTodos();

        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Desafio> resultado = repository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Implementar API REST", resultado.get().getDescricao());
        verify(jpaRepository, times(1)).findById(1L);
    }
}
