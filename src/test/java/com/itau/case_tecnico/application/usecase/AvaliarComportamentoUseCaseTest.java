package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliarComportamentoUseCaseTest {

    @Mock
    private AvaliacaoRepositoryPort avaliacaoRepository;

    @Mock
    private ColaboradorRepositoryPort colaboradorRepository;

    @InjectMocks
    private AvaliarComportamentoUseCase useCase;

    private AvaliacaoComportamental avaliacao;
    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        avaliacao = new AvaliacaoComportamental(1L, "Você trabalha com autonomia?", 4);
        
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("João Silva");
    }

    @Test
    void deveCriarAvaliacaoComSucesso() {
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));
        
        AvaliacaoComportamental avaliacaoComId = new AvaliacaoComportamental(1L, "Você trabalha com autonomia?", 4);
        avaliacaoComId.setId(1L);
        when(avaliacaoRepository.salvar(any(AvaliacaoComportamental.class))).thenReturn(avaliacaoComId);

        AvaliacaoComportamental resultado = useCase.executar(avaliacao);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(4, resultado.getNota());
        verify(colaboradorRepository, times(1)).buscarPorId(1L);
        verify(avaliacaoRepository, times(1)).salvar(any(AvaliacaoComportamental.class));
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExistir() {
        when(colaboradorRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> useCase.executar(avaliacao));
        verify(avaliacaoRepository, never()).salvar(any(AvaliacaoComportamental.class));
    }

    @Test
    void deveLancarExcecaoQuandoNotaForMaiorQue10() {
        avaliacao = new AvaliacaoComportamental(1L, "Tipo", 11);

        assertThrows(ValidacaoException.class, () -> useCase.executar(avaliacao));
        verify(avaliacaoRepository, never()).salvar(any(AvaliacaoComportamental.class));
    }

    @Test
    void deveLancarExcecaoQuandoNotaForMenorQue1() {
        avaliacao = new AvaliacaoComportamental(1L, "Tipo", 0);

        assertThrows(ValidacaoException.class, () -> useCase.executar(avaliacao));
        verify(avaliacaoRepository, never()).salvar(any(AvaliacaoComportamental.class));
    }
}
