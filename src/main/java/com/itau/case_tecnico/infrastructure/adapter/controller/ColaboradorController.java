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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {
    private static final Logger log = LoggerFactory.getLogger(ColaboradorController.class);
    
    private final CriarColaboradorUseCase criarColaboradorUseCase;
    private final CalcularNotaFinalUseCase calcularNotaFinalUseCase;
    private final ListarTodosUseCase listarTodosUseCase;
    private final BuscarPorIdUseCase buscarPorIdUseCase;

    public ColaboradorController(CriarColaboradorUseCase criarColaboradorUseCase,
                                 CalcularNotaFinalUseCase calcularNotaFinalUseCase,
                                 ListarTodosUseCase listarTodosUseCase,
                                 BuscarPorIdUseCase buscarPorIdUseCase) {
        this.criarColaboradorUseCase = criarColaboradorUseCase;
        this.calcularNotaFinalUseCase = calcularNotaFinalUseCase;
        this.listarTodosUseCase = listarTodosUseCase;
        this.buscarPorIdUseCase = buscarPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody ColaboradorRequest request) {
        log.info("Recebida requisição POST /api/colaboradores - matrícula: {}", request.getMatricula());
        try {
            Colaborador colaborador = new Colaborador(
                request.getMatricula(),
                request.getNome(),
                request.getDataAdmissao(),
                request.getCargo()
            );
            
            Colaborador salvo = criarColaboradorUseCase.executar(colaborador);
            log.info("Colaborador criado com sucesso via API: ID {}", salvo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao criar colaborador: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao criar colaborador", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar colaborador: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> listarTodos() {
        log.info("Recebida requisição GET /api/colaboradores");
        try {
            List<Colaborador> colaboradores = listarTodosUseCase.listarColaboradores();
            List<ColaboradorResponse> responses = colaboradores.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            log.info("Retornando {} colaboradores", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Erro ao listar colaboradores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao listar colaboradores: " + e.getMessage());
        }
    }

    @GetMapping("/{colaborador_id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable("colaborador_id") Long colaboradorId) {
        log.info("Recebida requisição GET /api/colaboradores/{}", colaboradorId);
        try {
            Optional<Colaborador> colaboradorOpt = buscarPorIdUseCase.buscarColaborador(colaboradorId);
            
            if (!colaboradorOpt.isPresent()) {
                log.warn("Colaborador não encontrado: ID {}", colaboradorId);
                return ResponseEntity.notFound().build();
            }
            
            log.debug("Colaborador encontrado: ID {}", colaboradorId);
            return ResponseEntity.ok(toResponse(colaboradorOpt.get()));
        } catch (Exception e) {
            log.error("Erro ao buscar colaborador ID {}", colaboradorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar colaborador: " + e.getMessage());
        }
    }

    @GetMapping("/{colaborador_id}/nota-final")
    public ResponseEntity<Object> calcularNotaFinal(@PathVariable("colaborador_id") Long colaboradorId) {
        log.info("Recebida requisição GET /api/colaboradores/{}/nota-final", colaboradorId);
        try {
            CalcularNotaFinalUseCase.NotaFinalDTO resultado = calcularNotaFinalUseCase.executar(colaboradorId);
            
            NotaFinalResponse response = new NotaFinalResponse();
            response.setColaboradorId(resultado.getColaboradorId());
            response.setNotaFinal(resultado.getNotaFinal());
            response.setMediaComportamental(resultado.getMediaComportamental());
            response.setMediaDesafios(resultado.getMediaDesafios());
            
            log.info("Nota final calculada com sucesso para colaborador ID {}: {}", 
                     colaboradorId, resultado.getNotaFinal());
            return ResponseEntity.ok(response);
        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao calcular nota final do colaborador ID {}: {}", 
                     colaboradorId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao calcular nota final do colaborador ID {}", colaboradorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao calcular nota final: " + e.getMessage());
        }
    }

    private ColaboradorResponse toResponse(Colaborador colaborador) {
        ColaboradorResponse response = new ColaboradorResponse();
        response.setId(colaborador.getId());
        response.setMatricula(colaborador.getMatricula());
        response.setNome(colaborador.getNome());
        response.setDataAdmissao(colaborador.getDataAdmissao());
        response.setCargo(colaborador.getCargo());
        return response;
    }
}
