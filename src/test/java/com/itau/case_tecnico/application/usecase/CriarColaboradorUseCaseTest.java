package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarColaboradorUseCaseTest {

    @Mock
    private ColaboradorRepositoryPort repositorio;

    @InjectMocks
    private CriarColaboradorUseCase useCase;

    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setMatricula("MAT001");
        colaborador.setNome("João Silva");
        colaborador.setDataAdmissao("2020-01-01");
        colaborador.setCargo("Desenvolvedor");
    }

    @Test
    void deveCriarColaboradorComSucesso() {
        Colaborador colaboradorComId = new Colaborador();
        colaboradorComId.setId(1L);
        colaboradorComId.setMatricula("MAT001");
        colaboradorComId.setNome("João Silva");
        colaboradorComId.setDataAdmissao("2020-01-01");
        colaboradorComId.setCargo("Desenvolvedor");
        
        when(repositorio.salvar(any(Colaborador.class))).thenReturn(colaboradorComId);

        Colaborador resultado = useCase.executar(colaborador);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("MAT001", resultado.getMatricula());
        assertEquals("João Silva", resultado.getNome());
        verify(repositorio, times(1)).salvar(any(Colaborador.class));
    }

    @Test
    void deveLancarExcecaoQuandoMatriculaForNula() {
        colaborador.setMatricula(null);

        assertThrows(ValidacaoException.class, () -> useCase.executar(colaborador));
        verify(repositorio, never()).salvar(any(Colaborador.class));
    }

    @Test
    void deveLancarExcecaoQuandoMatriculaForVazia() {
        colaborador.setMatricula("");

        assertThrows(ValidacaoException.class, () -> useCase.executar(colaborador));
        verify(repositorio, never()).salvar(any(Colaborador.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        colaborador.setNome(null);

        assertThrows(ValidacaoException.class, () -> useCase.executar(colaborador));
        verify(repositorio, never()).salvar(any(Colaborador.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        colaborador.setNome("");

        assertThrows(ValidacaoException.class, () -> useCase.executar(colaborador));
        verify(repositorio, never()).salvar(any(Colaborador.class));
    }
}
