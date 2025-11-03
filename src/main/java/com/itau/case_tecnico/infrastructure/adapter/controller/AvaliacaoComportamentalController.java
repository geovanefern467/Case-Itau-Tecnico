package com.itau.case_tecnico.infrastructure.adapter.controller;

import com.itau.case_tecnico.application.usecase.AvaliarComportamentoUseCase;
import com.itau.case_tecnico.application.usecase.BuscarPorIdUseCase;
import com.itau.case_tecnico.application.usecase.ListarTodosUseCase;
import com.itau.case_tecnico.application.usecase.ValidacaoException;
import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.infrastructure.adapter.dto.AvaliacaoComportamentalRequest;
import com.itau.case_tecnico.infrastructure.adapter.dto.AvaliacaoComportamentalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avaliacoes-comportamentais")
public class AvaliacaoComportamentalController {
    private static final Logger log = LoggerFactory.getLogger(AvaliacaoComportamentalController.class);
    
    private final AvaliarComportamentoUseCase avaliarComportamentoUseCase;
    private final ListarTodosUseCase listarTodosUseCase;
    private final BuscarPorIdUseCase buscarPorIdUseCase;

    public AvaliacaoComportamentalController(AvaliarComportamentoUseCase avaliarComportamentoUseCase,
                                             ListarTodosUseCase listarTodosUseCase,
                                             BuscarPorIdUseCase buscarPorIdUseCase) {
        this.avaliarComportamentoUseCase = avaliarComportamentoUseCase;
        this.listarTodosUseCase = listarTodosUseCase;
        this.buscarPorIdUseCase = buscarPorIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> avaliar(@RequestBody AvaliacaoComportamentalRequest request) {
        log.info("Recebida requisição POST /api/avaliacoes-comportamentais - colaborador ID: {}, tipo: {}", 
                 request.getColaboradorId(), request.getTipo());
        try {
            AvaliacaoComportamental avaliacao = new AvaliacaoComportamental(
                request.getColaboradorId(),
                request.getTipo(),
                request.getNota()
            );
            
            AvaliacaoComportamental salva = avaliarComportamentoUseCase.executar(avaliacao);
            log.info("Avaliação comportamental criada com sucesso via API: ID {}", salva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salva));
        } catch (ValidacaoException e) {
            log.warn("Erro de validação ao criar avaliação comportamental: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao salvar avaliação comportamental", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao salvar avaliação: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> listarTodas() {
        log.info("Recebida requisição GET /api/avaliacoes-comportamentais");
        try {
            List<AvaliacaoComportamental> avaliacoes = listarTodosUseCase.listarAvaliacoes();
            List<AvaliacaoComportamentalResponse> responses = avaliacoes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            log.info("Retornando {} avaliações comportamentais", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Erro ao listar avaliações comportamentais", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao listar avaliações: " + e.getMessage());
        }
    }

    @GetMapping("/colaboradores/{colaborador_id}")
    public ResponseEntity<Object> listarPorColaborador(@PathVariable("colaborador_id") Long colaboradorId) {
        log.info("Recebida requisição GET /api/avaliacoes-comportamentais/colaboradores/{}", colaboradorId);
        try {
            // Verificar se o colaborador existe antes de buscar avaliações
            if (!buscarPorIdUseCase.buscarColaborador(colaboradorId).isPresent()) {
                log.warn("Colaborador não encontrado: ID {}", colaboradorId);
                return ResponseEntity.notFound().build();
            }
            
            List<AvaliacaoComportamental> todasAvaliacoes = listarTodosUseCase.listarAvaliacoes();
            List<AvaliacaoComportamental> avaliacoesDoColaborador = todasAvaliacoes.stream()
                .filter(avaliacao -> avaliacao.getColaboradorId().equals(colaboradorId))
                .collect(Collectors.toList());
            
            List<AvaliacaoComportamentalResponse> responses = avaliacoesDoColaborador.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
            
            log.info("Retornando {} avaliações comportamentais do colaborador ID {}", responses.size(), colaboradorId);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            log.error("Erro ao buscar avaliações do colaborador ID {}", colaboradorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar avaliações: " + e.getMessage());
        }
    }

    private AvaliacaoComportamentalResponse toResponse(AvaliacaoComportamental avaliacao) {
        AvaliacaoComportamentalResponse response = new AvaliacaoComportamentalResponse();
        response.setId(avaliacao.getId());
        response.setColaboradorId(avaliacao.getColaboradorId());
        response.setTipo(avaliacao.getTipo());
        response.setNota(avaliacao.getNota());
        return response;
    }
}