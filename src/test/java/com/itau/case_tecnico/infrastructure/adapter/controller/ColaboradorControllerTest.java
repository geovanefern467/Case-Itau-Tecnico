package com.itau.case_tecnico.infrastructure.adapter.controller;

import com.itau.case_tecnico.application.usecase.CalcularNotaFinalUseCase;
import com.itau.case_tecnico.application.usecase.CriarColaboradorUseCase;
import com.itau.case_tecnico.application.usecase.ListarTodosUseCase;
import com.itau.case_tecnico.application.usecase.BuscarPorIdUseCase;
import com.itau.case_tecnico.application.usecase.ValidacaoException;
import com.itau.case_tecnico.domain.model.Colaborador;
import com.itau.case_tecnico.infrastructure.adapter.dto.ColaboradorRequest;
import com.itau.case_tecnico.infrastructure.adapter.dto.ColaboradorResponse;
import com.itau.case_tecnico.infrastructure.adapter.dto.NotaFinalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorControllerTest {

    @Mock
    private CriarColaboradorUseCase criarColaboradorUseCase;

    @Mock
    private CalcularNotaFinalUseCase calcularNotaFinalUseCase;

    @Mock
    private ListarTodosUseCase listarTodosUseCase;

    @Mock
    private BuscarPorIdUseCase buscarPorIdUseCase;

    @InjectMocks
    private ColaboradorController controller;

    private Colaborador colaboradorMock;
    private ColaboradorRequest requestMock;

    @BeforeEach
    void setUp() {
        colaboradorMock = new Colaborador();
        colaboradorMock.setId(1L);
        colaboradorMock.setMatricula("MAT001");
        colaboradorMock.setNome("João Silva");
        colaboradorMock.setDataAdmissao("2020-01-01");
        colaboradorMock.setCargo("Desenvolvedor");

        requestMock = new ColaboradorRequest();
        requestMock.setMatricula("MAT001");
        requestMock.setNome("João Silva");
        requestMock.setDataAdmissao("2020-01-01");
        requestMock.setCargo("Desenvolvedor");
    }

    @Test
    void deveCriarColaboradorComSucesso() {
        when(criarColaboradorUseCase.executar(any(Colaborador.class))).thenReturn(colaboradorMock);

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ColaboradorResponse);
        verify(criarColaboradorUseCase, times(1)).executar(any(Colaborador.class));
    }

    @Test
    void deveRetornarBadRequestQuandoValidacaoFalhar() {
        when(criarColaboradorUseCase.executar(any(Colaborador.class)))
            .thenThrow(new ValidacaoException("Matrícula inválida"));

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Matrícula inválida", response.getBody());
        verify(criarColaboradorUseCase, times(1)).executar(any(Colaborador.class));
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorrerErroInesperado() {
        when(criarColaboradorUseCase.executar(any(Colaborador.class)))
            .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = controller.criar(requestMock);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao criar colaborador"));
    }

    @Test
    void deveListarTodosColaboradoresComSucesso() {
        List<Colaborador> colaboradores = Arrays.asList(colaboradorMock);
        when(listarTodosUseCase.listarColaboradores()).thenReturn(colaboradores);

        ResponseEntity<Object> response = controller.listarTodos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        verify(listarTodosUseCase, times(1)).listarColaboradores();
    }

    @Test
    void deveRetornarErroAoListarColaboradores() {
        when(listarTodosUseCase.listarColaboradores())
            .thenThrow(new RuntimeException("Erro ao listar"));

        ResponseEntity<Object> response = controller.listarTodos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao listar colaboradores"));
    }

    @Test
    void deveBuscarColaboradorPorIdComSucesso() {
        when(buscarPorIdUseCase.buscarColaborador(1L)).thenReturn(Optional.of(colaboradorMock));

        ResponseEntity<Object> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ColaboradorResponse);
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(1L);
    }

    @Test
    void deveRetornarNotFoundQuandoColaboradorNaoExistir() {
        when(buscarPorIdUseCase.buscarColaborador(999L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = controller.buscarPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(buscarPorIdUseCase, times(1)).buscarColaborador(999L);
    }

    @Test
    void deveRetornarErroAoBuscarColaborador() {
        when(buscarPorIdUseCase.buscarColaborador(anyLong()))
            .thenThrow(new RuntimeException("Erro ao buscar"));

        ResponseEntity<Object> response = controller.buscarPorId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao buscar colaborador"));
    }

    @Test
    void deveCalcularNotaFinalComSucesso() {
        CalcularNotaFinalUseCase.NotaFinalDTO notaFinalDTO = new CalcularNotaFinalUseCase.NotaFinalDTO(
            1L, 8.5, 8.0, 9.0
        );
        when(calcularNotaFinalUseCase.executar(1L)).thenReturn(notaFinalDTO);

        ResponseEntity<Object> response = controller.calcularNotaFinal(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof NotaFinalResponse);
        NotaFinalResponse notaResponse = (NotaFinalResponse) response.getBody();
        assertEquals(8.5, notaResponse.getNotaFinal());
        verify(calcularNotaFinalUseCase, times(1)).executar(1L);
    }

    @Test
    void deveRetornarBadRequestQuandoValidacaoNotaFinalFalhar() {
        when(calcularNotaFinalUseCase.executar(anyLong()))
            .thenThrow(new ValidacaoException("Colaborador sem avaliações"));

        ResponseEntity<Object> response = controller.calcularNotaFinal(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Colaborador sem avaliações", response.getBody());
    }

    @Test
    void deveRetornarErroAoCalcularNotaFinal() {
        when(calcularNotaFinalUseCase.executar(anyLong()))
            .thenThrow(new RuntimeException("Erro ao calcular"));

        ResponseEntity<Object> response = controller.calcularNotaFinal(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Erro ao calcular nota final"));
    }
}