package com.itau.case_tecnico.infrastructure.adapter.controller;

import com.itau.case_tecnico.application.usecase.BuscarPorIdUseCase;
import com.itau.case_tecnico.application.usecase.CriarDesafioUseCase;
import com.itau.case_tecnico.application.usecase.ListarTodosUseCase;
import com.itau.case_tecnico.application.usecase.ValidacaoException;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.infrastructure.adapter.dto.DesafioRequest;
import com.itau.case_tecnico.infrastructure.adapter.dto.DesafioResponse;
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
class DesafioControllerTest {

    @Mock
    private CriarDesafioUseCase criarDesafioUseCase;

    @Mock
    private ListarTodosUseCase listarTodosUseCase;

    @Mock
    private BuscarPorIdUseCase buscarPorIdUseCase;

    @InjectMocks
    private DesafioController controller;

    private Desafio desafioMock;
    private DesafioRequest requestMock;
    private Colaborador colaboradorMock;

    @BeforeEach
    void setUp() {
        desafioMock = new Desafio(1L, "Implementar nova funcionalidade", 9);
        desafioMock.setId(1L);

        requestMock = new DesafioRequest();
        requestMock.setColaboradorId(1L);
        requestMock.setDescricao("Implementar nova funcionalidade");
        requestMock.setNota(9);

        colaboradorMock = new Colaborador();
        colaboradorMock.setId(1L);
        colaboradorMock.setMatricula("MAT001");
        colaboradorMock.setNome("João Silva");
        colaboradorMock.setDataAdmissao("2020-01-01");
        colaboradorMock.setCargo("Desenvolvedor");
    }

    @Test
    void deveCriarDesafioComSucesso() {
        when(criarDesafioUseCase.executar(any(Desafio.class))).thenReturn(desafioMock);

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof DesafioResponse);
        verify(criarDesafioUseCase, times(1)).executar(any(Desafio.class));
    }

    @Test
    void deveRetornarBadRequestQuandoValidacaoFalhar() {
        when(criarDesafioUseCase.executar(any(Desafio.class)))
            .thenThrow(new ValidacaoException("Descrição inválida"));

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Descrição inválida", response.getBody());
    }

    @Test
    void deveRetornarErroAoCriarDesafio() {
        when(criarDesafioUseCase.executar(any(Desafio.class)))
            .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao criar desafio"));
    }

    @Test
    void deveListarTodosDesafiosComSucesso() {
        List<Desafio> desafios = Arrays.asList(desafioMock);
        when(listarTodosUseCase.listarDesafios()).thenReturn(desafios);

        ResponseEntity<Object> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(listarTodosUseCase, times(1)).listarDesafios();
    }

    @Test
    void deveRetornarErroAoListarDesafios() {
        when(listarTodosUseCase.listarDesafios())
            .thenThrow(new RuntimeException("Erro ao listar"));

        ResponseEntity<Object> response = controller.listarTodos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao listar desafios"));
    }

    @Test
    void deveListarDesafiosPorColaboradorComSucesso() {
        when(buscarPorIdUseCase.buscarColaborador(1L)).thenReturn(Optional.of(colaboradorMock));
        when(listarTodosUseCase.listarDesafios()).thenReturn(Arrays.asList(desafioMock));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(1L);
        verify(listarTodosUseCase, times(1)).listarDesafios();
    }

    @Test
    void deveRetornarNotFoundQuandoColaboradorNaoExistir() {
        when(buscarPorIdUseCase.buscarColaborador(999L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = controller.listarPorColaborador(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(999L);
        verify(listarTodosUseCase, never()).listarDesafios();
    }

    @Test
    void deveRetornarListaVaziaQuandoColaboradorNaoTemDesafios() {
        Desafio outroDesafio = new Desafio(2L, "Outro desafio", 7);
        outroDesafio.setId(2L);
        
        when(buscarPorIdUseCase.buscarColaborador(1L)).thenReturn(Optional.of(colaboradorMock));
        when(listarTodosUseCase.listarDesafios()).thenReturn(Arrays.asList(outroDesafio));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        List<DesafioResponse> lista = (List<DesafioResponse>) response.getBody();
        assertTrue(lista.isEmpty());
    }

    @Test
    void deveRetornarErroAoListarDesafiosPorColaborador() {
        when(buscarPorIdUseCase.buscarColaborador(anyLong()))
            .thenThrow(new RuntimeException("Erro ao buscar"));

        ResponseEntity<Object> response = controller.listarPorColaborador(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao buscar desafios"));
    }
}
