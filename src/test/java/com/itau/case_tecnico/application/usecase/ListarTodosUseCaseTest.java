package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodosUseCaseTest {

    @Mock
    private ListarTodosUseCase useCase;

    @Test
    void deveListarTodosColaboradores() {
        Colaborador colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("João Silva");
        List<Colaborador> colaboradores = Arrays.asList(colaborador);
        
        when(useCase.listarColaboradores()).thenReturn(colaboradores);

        List<Colaborador> resultado = useCase.listarColaboradores();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        verify(useCase, times(1)).listarColaboradores();
    }

    @Test
    void deveListarTodasAvaliacoes() {
        AvaliacaoComportamental avaliacao = new AvaliacaoComportamental(1L, "Trabalho em equipe", 8);
        avaliacao.setId(1L);
        List<AvaliacaoComportamental> avaliacoes = Arrays.asList(avaliacao);
        
        when(useCase.listarAvaliacoes()).thenReturn(avaliacoes);

        List<AvaliacaoComportamental> resultado = useCase.listarAvaliacoes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Trabalho em equipe", resultado.get(0).getTipo());
        verify(useCase, times(1)).listarAvaliacoes();
    }

    @Test
    void deveListarTodosDesafios() {
        Desafio desafio = new Desafio(1L, "Implementar feature X", 9);
        desafio.setId(1L);
        List<Desafio> desafios = Arrays.asList(desafio);
        
        when(useCase.listarDesafios()).thenReturn(desafios);

        List<Desafio> resultado = useCase.listarDesafios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Implementar feature X", resultado.get(0).getDescricao());
        verify(useCase, times(1)).listarDesafios();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverColaboradores() {
        when(useCase.listarColaboradores()).thenReturn(Collections.emptyList());

        List<Colaborador> resultado = useCase.listarColaboradores();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
