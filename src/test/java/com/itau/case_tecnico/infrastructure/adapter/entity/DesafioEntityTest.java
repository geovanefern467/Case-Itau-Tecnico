package com.itau.case_tecnico.infrastructure.adapter.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DesafioEntityTest {

    @Test
    void deveCriarDesafioEntityComConstrutorCompleto() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(1L);
        colaborador.setMatricula("12345");
        colaborador.setNome("João Silva");
        colaborador.setDataAdmissao("2024-01-15");
        colaborador.setCargo("Dev");
        
        DesafioEntity entity = new DesafioEntity();
        entity.setId(1L);
        entity.setColaborador(colaborador);
        entity.setDescricao("Implementar API REST");
        entity.setNota(85);

        assertEquals(1L, entity.getId());
        assertEquals(colaborador, entity.getColaborador());
        assertEquals("Implementar API REST", entity.getDescricao());
        assertEquals(85, entity.getNota());
    }

    @Test
    void deveCriarDesafioEntityVazio() {
        DesafioEntity entity = new DesafioEntity();

        assertNull(entity.getId());
        assertNull(entity.getColaborador());
        assertNull(entity.getDescricao());
        assertNull(entity.getNota());
    }

    @Test
    void deveSetarValoresCorretamente() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(2L);
        colaborador.setMatricula("67890");
        colaborador.setNome("Maria Santos");
        colaborador.setDataAdmissao("2024-02-20");
        colaborador.setCargo("Analista");
        
        DesafioEntity entity = new DesafioEntity();

        entity.setId(10L);
        entity.setColaborador(colaborador);
        entity.setDescricao("Criar testes unitários");
        entity.setNota(95);

        assertEquals(10L, entity.getId());
        assertEquals(colaborador, entity.getColaborador());
        assertEquals("Criar testes unitários", entity.getDescricao());
        assertEquals(95, entity.getNota());
    }

    @Test
    void devePermitirDesafioSemId() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        DesafioEntity entity = new DesafioEntity();
        entity.setColaborador(colaborador);
        entity.setDescricao("Refatorar código");
        entity.setNota(70);

        assertNull(entity.getId());
        assertNotNull(entity.getColaborador());
        assertEquals("Refatorar código", entity.getDescricao());
        assertEquals(70, entity.getNota());
    }

    @Test
    void deveValidarTodosCampos() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(3L);
        colaborador.setMatricula("11111");
        colaborador.setNome("Pedro");
        colaborador.setDataAdmissao("2024-03-10");
        colaborador.setCargo("Tech Lead");
        
        DesafioEntity entity = new DesafioEntity();
        entity.setId(5L);
        entity.setColaborador(colaborador);
        entity.setDescricao("Deploy em produção");
        entity.setNota(100);

        assertAll(
            () -> assertEquals(5L, entity.getId()),
            () -> assertNotNull(entity.getColaborador()),
            () -> assertEquals("Pedro", entity.getColaborador().getNome()),
            () -> assertEquals("Deploy em produção", entity.getDescricao()),
            () -> assertEquals(100, entity.getNota())
        );
    }

    @Test
    void devePermitirNotaMinima() {
        DesafioEntity entity = new DesafioEntity();
        entity.setDescricao("Desafio básico");
        entity.setNota(0);

        assertEquals(0, entity.getNota());
    }

    @Test
    void devePermitirNotaMaxima() {
        DesafioEntity entity = new DesafioEntity();
        entity.setDescricao("Desafio complexo");
        entity.setNota(100);

        assertEquals(100, entity.getNota());
    }
}
