package com.itau.case_tecnico.infrastructure.adapter.repository;

import com.itau.case_tecnico.domain.model.Colaborador;
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
class SpringDataColaboradorRepositoryTest {

    @Mock
    private ColaboradorJpaRepository jpaRepository;

    @InjectMocks
    private SpringColaboradorRepositoryAdapter repository;

    private Colaborador colaborador;
    private ColaboradorEntity entity;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setMatricula("12345");
        colaborador.setNome("João Silva");
        colaborador.setDataAdmissao("2024-01-15");
        colaborador.setCargo("Desenvolvedor");

        entity = new ColaboradorEntity();
        entity.setId(1L);
        entity.setMatricula("12345");
        entity.setNome("João Silva");
        entity.setDataAdmissao("2024-01-15");
        entity.setCargo("Desenvolvedor");
    }

    @Test
    void deveSalvarColaboradorComSucesso() {
        when(jpaRepository.save(any(ColaboradorEntity.class))).thenReturn(entity);

        Colaborador resultado = repository.salvar(colaborador);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("12345", resultado.getMatricula());
        verify(jpaRepository, times(1)).save(any(ColaboradorEntity.class));
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Colaborador> resultado = repository.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioQuandoColaboradorNaoExiste() {
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Colaborador> resultado = repository.buscarPorId(999L);

        assertFalse(resultado.isPresent());
        verify(jpaRepository, times(1)).findById(999L);
    }

    @Test
    void deveListarTodosColaboradores() {
        ColaboradorEntity entity2 = new ColaboradorEntity();
        entity2.setId(2L);
        entity2.setMatricula("67890");
        entity2.setNome("Maria Santos");
        entity2.setDataAdmissao("2024-02-20");
        entity2.setCargo("Analista");
        
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity, entity2));

        List<Colaborador> resultado = repository.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaColaboradores() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList());

        List<Colaborador> resultado = repository.listarTodos();

        assertTrue(resultado.isEmpty());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deveVerificarSeExistePorMatricula() {
        when(jpaRepository.existsByMatricula("12345")).thenReturn(true);

        boolean existe = repository.existePorMatricula("12345");

        assertTrue(existe);
        verify(jpaRepository, times(1)).existsByMatricula("12345");
    }

    @Test
    void deveRetornarFalseQuandoMatriculaNaoExiste() {
        when(jpaRepository.existsByMatricula("99999")).thenReturn(false);

        boolean existe = repository.existePorMatricula("99999");

        assertFalse(existe);
        verify(jpaRepository, times(1)).existsByMatricula("99999");
    }

    @Test
    void deveBuscarPorMatriculaComSucesso() {
        when(jpaRepository.findByMatricula("12345")).thenReturn(Optional.of(entity));

        Optional<Colaborador> resultado = repository.buscarPorMatricula("12345");

        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(jpaRepository, times(1)).findByMatricula("12345");
    }
}
