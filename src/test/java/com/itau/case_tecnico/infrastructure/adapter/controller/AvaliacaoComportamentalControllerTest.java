package com.itau.case_tecnico.infrastructure.adapter.controller;

import com.itau.case_tecnico.application.usecase.AvaliarComportamentoUseCase;
import com.itau.case_tecnico.application.usecase.BuscarPorIdUseCase;
import com.itau.case_tecnico.application.usecase.ListarTodosUseCase;
import com.itau.case_tecnico.application.usecase.ValidacaoException;
import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.infrastructure.adapter.dto.AvaliacaoComportamentalRequest;
import com.itau.case_tecnico.infrastructure.adapter.dto.AvaliacaoComportamentalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliacaoComportamentalControllerTest {

    @Mock
    private AvaliarComportamentoUseCase avaliarComportamentoUseCase;

    @Mock
    private ListarTodosUseCase listarTodosUseCase;

    @Mock
    private BuscarPorIdUseCase buscarPorIdUseCase;

    @InjectMocks
    private AvaliacaoComportamentalController controller;

    private AvaliacaoComportamental avaliacaoMock;
    private AvaliacaoComportamentalRequest requestMock;
    private Colaborador colaboradorMock;

    @BeforeEach
    void setUp() {
        avaliacaoMock = new AvaliacaoComportamental(1L, "Trabalho em equipe", 8);
        avaliacaoMock.setId(1L);

        requestMock = new AvaliacaoComportamentalRequest();
        requestMock.setColaboradorId(1L);
        requestMock.setTipo("Trabalho em equipe");
        requestMock.setNota(8);

        colaboradorMock = new Colaborador();
        colaboradorMock.setId(1L);
        colaboradorMock.setMatricula("MAT001");
        colaboradorMock.setNome("João Silva");
        colaboradorMock.setDataAdmissao("2020-01-01");
        colaboradorMock.setCargo("Desenvolvedor");
    }

    @Test
    void deveCriarAvaliacaoComSucesso() {
        when(avaliarComportamentoUseCase.executar(any(AvaliacaoComportamental.class)))
            .thenReturn(avaliacaoMock);

        ResponseEntity<Object> response = controller.avaliar(requestMock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AvaliacaoComportamentalResponse);
        verify(avaliarComportamentoUseCase, times(1)).executar(any(AvaliacaoComportamental.class));
    }

    @Test
    void deveRetornarBadRequestQuandoValidacaoFalhar() {
        when(avaliarComportamentoUseCase.executar(any(AvaliacaoComportamental.class)))
            .thenThrow(new ValidacaoException("Nota inválida"));

        ResponseEntity<Object> response = controller.avaliar(requestMock);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Nota inválida", response.getBody());
    }

    @Test
    void deveRetornarErroAoCriarAvaliacao() {
        when(avaliarComportamentoUseCase.executar(any(AvaliacaoComportamental.class)))
            .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = controller.avaliar(requestMock);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao salvar avaliação"));
    }

    @Test
    void deveListarTodasAvaliacoesComSucesso() {
        List<AvaliacaoComportamental> avaliacoes = Arrays.asList(avaliacaoMock);
        when(listarTodosUseCase.listarAvaliacoes()).thenReturn(avaliacoes);

        ResponseEntity<Object> response = controller.listarTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(listarTodosUseCase, times(1)).listarAvaliacoes();
    }

    @Test
    void deveRetornarErroAoListarAvaliacoes() {
        when(listarTodosUseCase.listarAvaliacoes())
            .thenThrow(new RuntimeException("Erro ao listar"));

        ResponseEntity<Object> response = controller.listarTodas();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao listar avaliações"));
    }

    @Test
    void deveListarAvaliacoesPorColaboradorComSucesso() {
        when(buscarPorIdUseCase.buscarColaborador(1L)).thenReturn(Optional.of(colaboradorMock));
        when(listarTodosUseCase.listarAvaliacoes()).thenReturn(Arrays.asList(avaliacaoMock));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(1L);
        verify(listarTodosUseCase, times(1)).listarAvaliacoes();
    }

    @Test
    void deveRetornarNotFoundQuandoColaboradorNaoExistir() {
        when(buscarPorIdUseCase.buscarColaborador(999L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = controller.listarPorColaborador(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(999L);
        verify(listarTodosUseCase, never()).listarAvaliacoes();
    }

    @Test
    void deveRetornarListaVaziaQuandoColaboradorNaoTemAvaliacoes() {
        AvaliacaoComportamental outraAvaliacao = new AvaliacaoComportamental(2L, "Liderança", 9);
        outraAvaliacao.setId(2L);
        
        when(buscarPorIdUseCase.buscarColaborador(1L)).thenReturn(Optional.of(colaboradorMock));
        when(listarTodosUseCase.listarAvaliacoes()).thenReturn(Arrays.asList(outraAvaliacao));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<AvaliacaoComportamentalResponse> lista = (List<AvaliacaoComportamentalResponse>) response.getBody();
        assertTrue(lista.isEmpty());
    }

    @Test
    void deveRetornarErroAoListarAvaliacoesPorColaborador() {
        when(buscarPorIdUseCase.buscarColaborador(anyLong()))
            .thenThrow(new RuntimeException("Erro ao buscar"));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao buscar avaliações"));
    }
}
