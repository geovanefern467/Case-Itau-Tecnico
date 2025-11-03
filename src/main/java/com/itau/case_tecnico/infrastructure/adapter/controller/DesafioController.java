package com.itau.case_tecnico.infrastructure.adapter.controller;

import com.itau.case_tecnico.application.usecase.BuscarPorIdUseCase;
import com.itau.case_tecnico.application.usecase.CriarDesafioUseCase;
import com.itau.case_tecnico.application.usecase.ListarTodosUseCase;
import com.itau.case_tecnico.application.usecase.ValidacaoException;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.infrastructure.adapter.dto.DesafioRequest;
import com.itau.case_tecnico.infrastructure.adapter.dto.DesafioResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/desafios")
public class DesafioController {
    private static final Logger log = LoggerFactory.getLogger(DesafioController.class);
    
    private final CriarDesafioUseCase criarDesafioUseCase;
    private final ListarTodosUseCase listarTodosUseCase;
    private final BuscarPorIdUseCase buscarPorIdUseCase;

    public DesafioController(CriarDesafioUseCase criarDesafioUseCase,
                             ListarTodosUseCase listarTodosUseCase,
                             BuscarPorIdUseCase buscarPorIdUseCase) {
        this.criarDesafioUseCase = criarDesafioUseCase;
        this.listarTodosUseCase = listarTodosUseCase;
        this.buscarPorIdUseCase = buscarPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody DesafioRequest request) {
        log.info("Recebida requisição POST /api/desafios - colaborador ID: {}", request.getColaboradorId());
        try {
            Desafio desafio = new Desafio(
                request.getColaboradorId(),
                request.getDescricao(),
                request.getNota()
            );
            
            Desafio salvo = criarDesafioUseCase.executar(desafio);
            log.info("Desafio criado com sucesso via API: ID {}", salvo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao criar desafio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao criar desafio", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar desafio: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> listarTodos() {
        log.info("Recebida requisição GET /api/desafios");
        try {
            List<Desafio> desafios = listarTodosUseCase.listarDesafios();
            List<DesafioResponse> responses = desafios.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            log.info("Retornando {} desafios", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Erro ao listar desafios", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao listar desafios: " + e.getMessage());
        }
    }

    @GetMapping("/colaboradores/{colaborador_id}")
    public ResponseEntity<Object> listarPorColaborador(@PathVariable("colaborador_id") Long colaboradorId) {
        log.info("Recebida requisição GET /api/desafios/colaboradores/{}", colaboradorId);
        try {
            // Verificar se o colaborador existe antes de buscar desafios
            if (!buscarPorIdUseCase.buscarColaborador(colaboradorId).isPresent()) {
                log.warn("Colaborador não encontrado: ID {}", colaboradorId);
                return ResponseEntity.notFound().build();
            }
            
            List<Desafio> todosDesafios = listarTodosUseCase.listarDesafios();
            List<Desafio> desafiosDoColaborador = todosDesafios.stream()
                .filter(desafio -> desafio.getColaboradorId().equals(colaboradorId))
                .collect(Collectors.toList());
            
            List<DesafioResponse> responses = desafiosDoColaborador.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            
            log.info("Retornando {} desafios do colaborador ID {}", responses.size(), colaboradorId);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            log.error("Erro ao buscar desafios do colaborador ID {}", colaboradorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar desafios: " + e.getMessage());
        }
    }

    private DesafioResponse toResponse(Desafio desafio) {
        DesafioResponse response = new DesafioResponse();
        response.setId(desafio.getId());
        response.setColaboradorId(desafio.getColaboradorId());
        response.setDescricao(desafio.getDescricao());
        response.setNota(desafio.getNota());
        return response;
    }
}