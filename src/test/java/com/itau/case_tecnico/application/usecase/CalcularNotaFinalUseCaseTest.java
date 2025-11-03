package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcularNotaFinalUseCaseTest {

    @Mock
    private ColaboradorRepositoryPort colaboradorRepository;

    @Mock
    private AvaliacaoRepositoryPort avaliacaoRepository;

    @Mock
    private DesafioRepositoryPort desafioRepository;

    @InjectMocks
    private CalcularNotaFinalUseCase useCase;

    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setMatricula("MAT001");
        colaborador.setNome("João Silva");
        colaborador.setDataAdmissao("2020-01-01");
        colaborador.setCargo("Desenvolvedor");
    }

    @Test
    void deveCalcularNotaFinalComSucesso() {
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));
        
        // O sistema exige exatamente 4 avaliações comportamentais com tipos específicos e notas entre 1-5
        List<AvaliacaoComportamental> avaliacoes = Arrays.asList(
            criarAvaliacao(1L, "Você promove um ambiente colaborativo?", 4),
            criarAvaliacao(1L, "Você se atualiza e aprende o tempo todo?", 5),
            criarAvaliacao(1L, "Você utiliza dados para tomar decisões?", 3),
            criarAvaliacao(1L, "Você trabalha com autonomia?", 4)
        );
        when(avaliacaoRepository.buscarPorColaboradorId(1L)).thenReturn(avaliacoes);
        
        // Notas dos desafios devem ser entre 1 e 5
        List<Desafio> desafios = Arrays.asList(
            criarDesafio(1L, "Descrição 1", 3),
            criarDesafio(1L, "Descrição 2", 4)
        );
        when(desafioRepository.buscarPorColaboradorId(1L)).thenReturn(desafios);

        CalcularNotaFinalUseCase.NotaFinalDTO resultado = useCase.executar(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getColaboradorId());
        assertTrue(resultado.getNotaFinal() > 0);
        assertTrue(resultado.getMediaComportamental() > 0);
        assertTrue(resultado.getMediaDesafios() > 0);
        verify(colaboradorRepository, times(1)).buscarPorId(1L);
        verify(avaliacaoRepository, times(1)).buscarPorColaboradorId(1L);
        verify(desafioRepository, times(1)).buscarPorColaboradorId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExistir() {
        when(colaboradorRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> useCase.executar(999L));
        verify(colaboradorRepository, times(1)).buscarPorId(999L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverAvaliacoesSuficientes() {
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));
        
        // Menos de 4 avaliações
        List<AvaliacaoComportamental> avaliacoes = Arrays.asList(
            criarAvaliacao(1L, "Você trabalha com autonomia?", 4),
            criarAvaliacao(1L, "Você promove um ambiente colaborativo?", 5)
        );
        when(avaliacaoRepository.buscarPorColaboradorId(1L)).thenReturn(avaliacoes);
        when(desafioRepository.buscarPorColaboradorId(1L)).thenReturn(Arrays.asList());

        assertThrows(ValidacaoException.class, () -> useCase.executar(1L));
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverAvaliacoes() {
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));
        when(avaliacaoRepository.buscarPorColaboradorId(1L)).thenReturn(Arrays.asList());
        when(desafioRepository.buscarPorColaboradorId(1L)).thenReturn(Arrays.asList());

        assertThrows(ValidacaoException.class, () -> useCase.executar(1L));
    }

    private AvaliacaoComportamental criarAvaliacao(Long colaboradorId, String tipo, int nota) {
        AvaliacaoComportamental avaliacao = new AvaliacaoComportamental(colaboradorId, tipo, nota);
        avaliacao.setId(1L);
        return avaliacao;
    }

    private Desafio criarDesafio(Long colaboradorId, String descricao, int nota) {
        Desafio desafio = new Desafio(colaboradorId, descricao, nota);
        desafio.setId(1L);
        return desafio;
    }
}
