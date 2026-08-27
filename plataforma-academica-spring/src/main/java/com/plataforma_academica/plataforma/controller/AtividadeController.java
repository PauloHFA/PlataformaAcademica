package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.dto.AtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.AtividadeMapper;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.service.AtividadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/atividades")
@CrossOrigin(origins = "http://localhost:4200")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @PostMapping("/sala/{salaId}/autor/{autorId}")
    public ResponseEntity<AtividadeResponseDTO> criarAtividade(
            @PathVariable Long salaId,
            @PathVariable Long autorId,
            @RequestBody AtividadeDTO dto) {
        Atividade atividadeCriada = atividadeService.criarAtividade(salaId, dto, autorId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividadeCriada));
    }

    @GetMapping("/{atividadeId}")
    public ResponseEntity<AtividadeResponseDTO> buscarAtividadePorId(
            @PathVariable Long atividadeId) {
        Atividade atividade = atividadeService.buscarAtividadePorId(atividadeId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<AtividadeResponseDTO>> listarPorSala(
            @PathVariable Long salaId) {
        List<Atividade> lista = atividadeService.listarAtividadesPorSala(salaId);
        return ResponseEntity.ok(lista.stream().map(AtividadeMapper::toResponse).toList());
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<AtividadeResponseDTO>> listarPorAutor(
            @PathVariable Long autorId) {
        List<Atividade> lista = atividadeService.listarAtividadesPorAutor(autorId);
        return ResponseEntity.ok(lista.stream().map(AtividadeMapper::toResponse).toList());
    }

    @PutMapping("/{atividadeId}/autor/{autorId}")
    public ResponseEntity<AtividadeResponseDTO> atualizarAtividade(
            @PathVariable Long atividadeId,
            @PathVariable Long autorId,
            @RequestBody AtividadeDTO atividadeAtualizada) {
        Atividade atividade = atividadeService.atualizarAtividade(atividadeId, atividadeAtualizada, autorId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
    }

    @DeleteMapping("/{atividadeId}/autor/{autorId}")
    public ResponseEntity<Void> deletarAtividade(
            @PathVariable Long atividadeId,
            @PathVariable Long autorId) {
        atividadeService.deletarAtividade(atividadeId, autorId);
        return ResponseEntity.noContent().build();
    }
}
