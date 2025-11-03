package com.itau.case_tecnico.infrastructure.adapter.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaboradorEntityTest {

    @Test
    void deveCriarColaboradorEntityComConstrutorCompleto() {
        ColaboradorEntity entity = new ColaboradorEntity();
        entity.setId(1L);
        entity.setMatricula("12345");
        entity.setNome("João Silva");
        entity.setDataAdmissao("2024-01-15");
        entity.setCargo("Desenvolvedor");

        assertEquals(1L, entity.getId());
        assertEquals("12345", entity.getMatricula());
        assertEquals("João Silva", entity.getNome());
        assertEquals("2024-01-15", entity.getDataAdmissao());
        assertEquals("Desenvolvedor", entity.getCargo());
    }

    @Test
    void deveCriarColaboradorEntityVazio() {
        ColaboradorEntity entity = new ColaboradorEntity();

        assertNull(entity.getId());
        assertNull(entity.getMatricula());
        assertNull(entity.getNome());
        assertNull(entity.getDataAdmissao());
        assertNull(entity.getCargo());
    }

    @Test
    void deveSetarValoresCorretamente() {
        ColaboradorEntity entity = new ColaboradorEntity();

        entity.setId(10L);
        entity.setMatricula("99999");
        entity.setNome("Maria Santos");
        entity.setDataAdmissao("2024-06-20");
        entity.setCargo("Analista");

        assertEquals(10L, entity.getId());
        assertEquals("99999", entity.getMatricula());
        assertEquals("Maria Santos", entity.getNome());
        assertEquals("2024-06-20", entity.getDataAdmissao());
        assertEquals("Analista", entity.getCargo());
    }
}
