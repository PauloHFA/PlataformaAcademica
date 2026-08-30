package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.FrequenciaRequestDTO;
import com.plataforma_academica.plataforma.model.Frequencia;
import com.plataforma_academica.plataforma.service.FrequenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller REST responsável por Frequência acadêmica.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Academic / Controle de presença em sala de aula.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see FrequenciaService
 * @see REQ-025 (Controle de Frequência)
 */
@RestController
@RequestMapping("/api/frequencia")
@CrossOrigin(origins = "http://localhost:4200")
public class FrequenciaController {

    private final FrequenciaService frequenciaService;

    public FrequenciaController(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarFrequencia(@RequestBody FrequenciaRequestDTO dto) {
        if (dto.getAlunoId() == null || dto.getSalaId() == null || dto.getData() == null || dto.getPresente() == null) {
            return ResponseEntity.badRequest().body("Campos obrigatórios ausentes: alunoId, salaId, data, presente.");
        }

        Frequencia frequencia = frequenciaService.registrarFrequencia(dto.getAlunoId(), dto.getSalaId(), dto.getData(),
                dto.getPresente(), dto.getJustificativa());
        return ResponseEntity.ok(frequencia);
    }

    @GetMapping
    public ResponseEntity<?> buscarFrequencias(
            @RequestParam UUID alunoId,
            @RequestParam UUID salaId,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            if (inicio != null && fim != null) {
                LocalDate dataInicio = LocalDate.parse(inicio);
                LocalDate dataFim = LocalDate.parse(fim);
                List<Frequencia> retorno = frequenciaService.buscarFrequencias(alunoId, salaId, dataInicio, dataFim);
                return ResponseEntity.ok(retorno);
            } else {
                List<Frequencia> retorno = frequenciaService.buscarFrequencias(alunoId, salaId);
                return ResponseEntity.ok(retorno);
            }
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Formato de data inválido para inicio/fim. Use yyyy-MM-dd.");
        }
    }

    @GetMapping("/percentual")
    public ResponseEntity<?> percentualPresenca(
            @RequestParam UUID alunoId,
            @RequestParam UUID salaId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDate dataInicio = LocalDate.parse(inicio);
            LocalDate dataFim = LocalDate.parse(fim);
            double percentual = frequenciaService.calcularPercentualPresenca(alunoId, salaId, dataInicio, dataFim);
            return ResponseEntity.ok(percentual);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Formato de data inválido para inicio/fim. Use yyyy-MM-dd.");
        }
    }
}
