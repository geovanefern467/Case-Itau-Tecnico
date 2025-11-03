package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarPorIdUseCaseTest {

    @Mock
    private ColaboradorRepositoryPort colaboradorRepository;

    @Mock
    private AvaliacaoRepositoryPort avaliacaoRepository;

    @Mock
    private DesafioRepositoryPort desafioRepository;

    @InjectMocks
    private BuscarPorIdUseCase useCase;

    @Test
    void deveBuscarColaboradorPorId() {
        Colaborador colaborador = new Colaborador();
        colaborador.setId(1L);
        
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));

        Optional<Colaborador> resultado = useCase.buscarColaborador(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(colaboradorRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void deveBuscarAvaliacaoPorId() {
        AvaliacaoComportamental avaliacao = new AvaliacaoComportamental(1L, "Tipo", 8);
        avaliacao.setId(1L);
        
        when(avaliacaoRepository.buscarPorId(1L)).thenReturn(Optional.of(avaliacao));

        Optional<AvaliacaoComportamental> resultado = useCase.buscarAvaliacao(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(avaliacaoRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void deveBuscarDesafioPorId() {
        Desafio desafio = new Desafio(1L, "Descrição", 9);
        desafio.setId(1L);
        
        when(desafioRepository.buscarPorId(1L)).thenReturn(Optional.of(desafio));

        Optional<Desafio> resultado = useCase.buscarDesafio(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(desafioRepository, times(1)).buscarPorId(1L);
    }
}
