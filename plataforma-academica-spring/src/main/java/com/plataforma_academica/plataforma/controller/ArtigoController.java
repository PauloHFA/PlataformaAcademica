package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.dto.ArtigoResponseDTO;
import com.plataforma_academica.plataforma.mapper.ArtigoMapper;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.service.ArtigoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/artigos")
@CrossOrigin(origins = "http://localhost:4200")
public class ArtigoController {

    private final ArtigoService artigoService;

    public ArtigoController(ArtigoService artigoService) {
        this.artigoService = artigoService;
    }

    @PostMapping
    public ResponseEntity<ArtigoResponseDTO> criar(@Valid @RequestBody ArtigoDTO dto) {
        Artigo artigo = artigoService.criar(dto);
        ArtigoResponseDTO response = ArtigoMapper.toResponse(artigo);
        return ResponseEntity
                .created(URI.create("/api/artigos/" + artigo.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtigoResponseDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody ArtigoDTO dto) {
        Artigo artigo = artigoService.editar(id, dto);
        return ResponseEntity.ok(ArtigoMapper.toResponse(artigo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam Long solicitanteId) {
        artigoService.deletar(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtigoResponseDTO> buscar(@PathVariable Long id) {
        Artigo artigo = artigoService.buscarPorId(id);
        return ResponseEntity.ok(ArtigoMapper.toResponse(artigo));
    }

    @GetMapping
    public ResponseEntity<List<ArtigoResponseDTO>> listar() {
        List<Artigo> lista = artigoService.listarTodos();
        return ResponseEntity.ok(lista.stream().map(ArtigoMapper::toResponse).toList());
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<ArtigoResponseDTO>> porAutor(@PathVariable Long autorId) {
        List<Artigo> lista = artigoService.listarPorAutor(autorId);
        return ResponseEntity.ok(lista.stream().map(ArtigoMapper::toResponse).toList());
    }
}
