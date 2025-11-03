package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
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
class CriarDesafioUseCaseTest {

    @Mock
    private DesafioRepositoryPort desafioRepository;

    @Mock
    private ColaboradorRepositoryPort colaboradorRepository;

    @InjectMocks
    private CriarDesafioUseCase useCase;

    private Desafio desafio;
    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        desafio = new Desafio(1L, "Implementar feature X", 4);
        
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("João Silva");
    }

    @Test
    void deveCriarDesafioComSucesso() {
        when(colaboradorRepository.buscarPorId(1L)).thenReturn(Optional.of(colaborador));
        
        Desafio desafioComId = new Desafio(1L, "Implementar feature X", 4);
        desafioComId.setId(1L);
        when(desafioRepository.salvar(any(Desafio.class))).thenReturn(desafioComId);

        Desafio resultado = useCase.executar(desafio);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(4, resultado.getNota());
        verify(colaboradorRepository, times(1)).buscarPorId(1L);
        verify(desafioRepository, times(1)).salvar(any(Desafio.class));
    }

    @Test
    void deveLancarExcecaoQuandoColaboradorNaoExistir() {
        when(colaboradorRepository.buscarPorId(anyLong())).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> useCase.executar(desafio));
        verify(desafioRepository, never()).salvar(any(Desafio.class));
    }

    @Test
    void deveLancarExcecaoQuandoNotaForMaiorQue10() {
        desafio = new Desafio(1L, "Descrição", 11);

        assertThrows(ValidacaoException.class, () -> useCase.executar(desafio));
        verify(desafioRepository, never()).salvar(any(Desafio.class));
    }

    @Test
    void deveLancarExcecaoQuandoNotaForMenorQue1() {
        desafio = new Desafio(1L, "Descrição", 0);

        assertThrows(ValidacaoException.class, () -> useCase.executar(desafio));
        verify(desafioRepository, never()).salvar(any(Desafio.class));
    }
}
