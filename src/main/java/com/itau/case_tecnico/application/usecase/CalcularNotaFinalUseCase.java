package com.itau.case_tecnico.application.usecase;

import com.itau.case_tecnico.domain.model.AvaliacaoComportamental;
import com.itau.case_tecnico.domain.model.Desafio;
import com.itau.case_tecnico.domain.port.AvaliacaoRepositoryPort;
import com.itau.case_tecnico.domain.port.ColaboradorRepositoryPort;
import com.itau.case_tecnico.domain.port.DesafioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalcularNotaFinalUseCase {
    private static final Logger log = LoggerFactory.getLogger(CalcularNotaFinalUseCase.class);
    private final ColaboradorRepositoryPort colaboradorRepository;
    private final AvaliacaoRepositoryPort avaliacaoRepository;
    private final DesafioRepositoryPort desafioRepository;

    public CalcularNotaFinalUseCase(ColaboradorRepositoryPort colaboradorRepository,
                                    AvaliacaoRepositoryPort avaliacaoRepository,
                                    DesafioRepositoryPort desafioRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.desafioRepository = desafioRepository;
    }

    public NotaFinalDTO executar(Long colaboradorId) {
        log.info("Iniciando cálculo de nota final para colaborador ID: {}", colaboradorId);
        
        if (!colaboradorRepository.buscarPorId(colaboradorId).isPresent()) {
            log.error("Colaborador não encontrado: ID {}", colaboradorId);
            throw new ValidacaoException("Colaborador não encontrado");
        }

        List<AvaliacaoComportamental> avaliacoes = avaliacaoRepository.buscarPorColaboradorId(colaboradorId);
        List<Desafio> desafios = desafioRepository.buscarPorColaboradorId(colaboradorId);

        log.debug("Colaborador ID {} possui {} avaliações e {} desafios", colaboradorId, avaliacoes.size(), desafios.size());

        if (avaliacoes.size() != 4) {
            log.error("Colaborador ID {} possui {} avaliações (esperado: 4)", colaboradorId, avaliacoes.size());
            throw new ValidacaoException("Colaborador deve ter exatamente 4 avaliações comportamentais");
        }

        if (desafios.size() < 2 || desafios.size() > 4) {
            log.error("Colaborador ID {} possui {} desafios (esperado: entre 2 e 4)", colaboradorId, desafios.size());
            throw new ValidacaoException("Colaborador deve ter entre 2 e 4 desafios");
        }

        Double mediaComportamental = avaliacoes.stream()
            .mapToInt(AvaliacaoComportamental::getNota)
            .average()
            .orElse(0.0);

        Double mediaDesafios = desafios.stream()
            .filter(d -> d.getNota() != null)
            .mapToInt(Desafio::getNota)
            .average()
            .orElse(0.0);

        Double notaFinal = (mediaComportamental + mediaDesafios) / 2.0;
        
        log.info("Nota final calculada para colaborador ID {}: {} (Comportamental: {}, Desafios: {})", 
                 colaboradorId, notaFinal, mediaComportamental, mediaDesafios);
        
        return new NotaFinalDTO(colaboradorId, notaFinal, mediaComportamental, mediaDesafios);
    }

    public static class NotaFinalDTO {
        private final Long colaboradorId;
        private final Double notaFinal;
        private final Double mediaComportamental;
        private final Double mediaDesafios;

        public NotaFinalDTO(Long colaboradorId, Double notaFinal, Double mediaComportamental, Double mediaDesafios) {
            this.colaboradorId = colaboradorId;
            this.notaFinal = notaFinal;
            this.mediaComportamental = mediaComportamental;
            this.mediaDesafios = mediaDesafios;
        }

        public Long getColaboradorId() { return colaboradorId; }
        public Double getNotaFinal() { return notaFinal; }
        public Double getMediaComportamental() { return mediaComportamental; }
        public Double getMediaDesafios() { return mediaDesafios; }
    }
}
