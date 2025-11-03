package com.itau.case_tecnico.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaboradorTest {

    @Test
    void deveCriarColaboradorComDadosValidos() {
        Colaborador colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setMatricula("12345");
        colaborador.setNome("João Silva");
        colaborador.setDataAdmissao("2024-01-01");
        colaborador.setCargo("Desenvolvedor");

        assertEquals(1L, colaborador.getId());
        assertEquals("12345", colaborador.getMatricula());
        assertEquals("João Silva", colaborador.getNome());
        assertEquals("2024-01-01", colaborador.getDataAdmissao());
        assertEquals("Desenvolvedor", colaborador.getCargo());
    }

    @Test
    void devePermitirAlterarDadosDoColaborador() {
        Colaborador colaborador = new Colaborador();
        colaborador.setMatricula("11111");
        colaborador.setNome("Maria");
        colaborador.setDataAdmissao("2024-02-15");
        colaborador.setCargo("Analista");
        
        colaborador.setId(5L);
        colaborador.setMatricula("22222");
        colaborador.setNome("Maria Santos");
        colaborador.setCargo("Gerente");

        assertEquals(5L, colaborador.getId());
        assertEquals("22222", colaborador.getMatricula());
        assertEquals("Maria Santos", colaborador.getNome());
        assertEquals("Gerente", colaborador.getCargo());
    }

    @Test
    void devePermitirColaboradorSemId() {
        Colaborador colaborador = new Colaborador();
        colaborador.setMatricula("12345");
        colaborador.setNome("João");
        colaborador.setDataAdmissao("2024-03-20");
        colaborador.setCargo("Dev");

        assertNull(colaborador.getId());
        assertNotNull(colaborador.getMatricula());
        assertEquals("João", colaborador.getNome());
    }

    @Test
    void deveValidarGettersESetters() {
        Colaborador colaborador = new Colaborador();

        colaborador.setId(10L);
        colaborador.setMatricula("99999");
        colaborador.setNome("Pedro Oliveira");
        colaborador.setDataAdmissao("2024-06-15");
        colaborador.setCargo("Tech Lead");

        assertAll(
            () -> assertEquals(10L, colaborador.getId()),
            () -> assertEquals("99999", colaborador.getMatricula()),
            () -> assertEquals("Pedro Oliveira", colaborador.getNome()),
            () -> assertEquals("2024-06-15", colaborador.getDataAdmissao()),
            () -> assertEquals("Tech Lead", colaborador.getCargo())
        );
    }
}
