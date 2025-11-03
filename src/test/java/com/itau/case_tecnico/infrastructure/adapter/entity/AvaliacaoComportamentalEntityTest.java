package com.itau.case_tecnico.infrastructure.adapter.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvaliacaoComportamentalEntityTest {

    @Test
    void deveCriarAvaliacaoEntityVazia() {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getColaborador());
        assertNull(entity.getTipo());
        assertNull(entity.getNota());
    }

    @Test
    void devePermitirSetarId() {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setId(1L);

        assertEquals(1L, entity.getId());
    }

    @Test
    void devePermitirSetarColaborador() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(1L);
        colaborador.setMatricula("12345");
        colaborador.setNome("João Silva");
        
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setColaborador(colaborador);

        assertNotNull(entity.getColaborador());
        assertEquals(1L, entity.getColaborador().getId());
        assertEquals("João Silva", entity.getColaborador().getNome());
    }

    @Test
    void devePermitirSetarTipoENota() {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setTipo("Ambiente colaborativo");
        entity.setNota(5);

        assertEquals("Ambiente colaborativo", entity.getTipo());
        assertEquals(5, entity.getNota());
    }

    @Test
    void deveCriarAvaliacaoCompleta() {
        ColaboradorEntity colaborador = new ColaboradorEntity();
        colaborador.setId(1L);
        colaborador.setMatricula("12345");
        colaborador.setNome("Maria Santos");
        colaborador.setDataAdmissao("2024-01-15");
        colaborador.setCargo("Desenvolvedora");

        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setId(10L);
        entity.setColaborador(colaborador);
        entity.setTipo("Aprende o tempo todo");
        entity.setNota(4);

        assertAll(
            () -> assertEquals(10L, entity.getId()),
            () -> assertNotNull(entity.getColaborador()),
            () -> assertEquals("Maria Santos", entity.getColaborador().getNome()),
            () -> assertEquals("Aprende o tempo todo", entity.getTipo()),
            () -> assertEquals(4, entity.getNota())
        );
    }

    @Test
    void devePermitirNotaMinima() {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setTipo("Usa dados para decisões");
        entity.setNota(1);

        assertEquals(1, entity.getNota());
    }

    @Test
    void devePermitirNotaMaxima() {
        AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
        entity.setTipo("Entrega resultados sustentáveis");
        entity.setNota(5);

        assertEquals(5, entity.getNota());
    }

    @Test
    void deveValidarTodosOsTiposDeAvaliacao() {
        String[] tipos = {
            "Ambiente colaborativo",
            "Aprende o tempo todo",
            "Usa dados para decisões",
            "Entrega resultados sustentáveis"
        };

        for (String tipo : tipos) {
            AvaliacaoComportamentalEntity entity = new AvaliacaoComportamentalEntity();
            entity.setTipo(tipo);
            entity.setNota(5);

            assertEquals(tipo, entity.getTipo());
            assertEquals(5, entity.getNota());
        }
    }
}
