package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.service.ComunidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comunidades")
@CrossOrigin(origins = "http://localhost:4200")
public class ComunidadeController {
    private final ComunidadeService comunidadeService;

    public ComunidadeController(ComunidadeService comunidadeService) {
        this.comunidadeService = comunidadeService;
    }

    @PostMapping
    public ResponseEntity<Comunidade> criar(@Valid @RequestBody ComunidadeDTO dto) {
        Comunidade c = comunidadeService.criarComunidade(dto);
        return ResponseEntity.created(URI.create("/api/comunidades/" + c.getId())).body(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @RequestParam Long solicitanteId) {
        comunidadeService.deletarComunidade(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/entrar")
    public ResponseEntity<MembroComunidade> entrar(@PathVariable Long id, @RequestParam Long usuarioId) {
        return ResponseEntity.ok(comunidadeService.entrarComunidade(id, usuarioId));
    }

    @PostMapping("/{id}/sair")
    public ResponseEntity<Void> sair(@PathVariable Long id, @RequestParam Long usuarioId) {
        comunidadeService.sairComunidade(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Comunidade>> listar() {
        return ResponseEntity.ok(comunidadeService.listarTodas());
    }

    @GetMapping("/{id}/membros")
    public ResponseEntity<List<MembroComunidade>> membros(@PathVariable Long id) {
        return ResponseEntity.ok(comunidadeService.listarMembros(id));
    }
}
