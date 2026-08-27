package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.TipoDestinoComentario;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.ComentarioService;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST responsável pelo gerenciamento de Comentários.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Gerencia operações HTTP (CRUD) para comentários em
 * postagens, atividades e salas.
 * Padrões aplicados: RestController, CrossOrigin, Spring Data.
 * 
 * @see ComentarioService
 * @see REQ-005 (Gerenciamento de Comentários)
 */
@RestController
@RequestMapping("/comentario")
@CrossOrigin(origins = "http://localhost:4200")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaladeAulaRepository salaRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private com.plataforma_academica.plataforma.repository.PostagemRepository postagemRepository;

    @GetMapping
    public ResponseEntity<List<Comentario>> listarTodos() {
        return ResponseEntity.ok(comentarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> buscarPorId(@PathVariable Long id) {
        Comentario comentario = comentarioService.buscarPorId(id);
        if (comentario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comentario);
    }

    @PostMapping
    public ResponseEntity<Comentario> salvar(@RequestBody Comentario comentario) {
        System.out.println("[POST /comentario] Tipo=" + comentario.getTipoDestino() + ", Autor="
                + (comentario.getAutor() != null ? comentario.getAutor().getId() : "null"));

        if (comentario.getAutor() != null && comentario.getAutor().getId() != null) {
            Usuario autor = usuarioRepository.findById(comentario.getAutor().getId())
                    .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
            comentario.setAutor(autor);
        }

        if (comentario.getSaladeAula() != null && comentario.getSaladeAula().getId() != null) {
            SaladeAula sala = salaRepository.findById(comentario.getSaladeAula().getId())
                    .orElseThrow(() -> new RuntimeException("Sala não encontrada"));
            comentario.setSaladeAula(sala);
        }

        if (comentario.getAtividade() != null && comentario.getAtividade().getId() != null) {
            Atividade atividade = atividadeRepository.findById(comentario.getAtividade().getId())
                    .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
            comentario.setAtividade(atividade);
        }

        if (comentario.getPostagem() != null && comentario.getPostagem().getId() != null) {
            com.plataforma_academica.plataforma.model.Postagem postagem = postagemRepository
                    .findById(comentario.getPostagem().getId())
                    .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
            comentario.setPostagem(postagem);
        }

        Comentario salvo = comentarioService.salvar(comentario);
        System.out.println("[POST /comentario] Sucesso: ID=" + salvo.getId());
        URI location = URI.create("/comentario/" + salvo.getId());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> atualizar(
            @PathVariable Long id,
            @RequestBody Comentario comentarioAtualizado) {
        Comentario atualizado = comentarioService.atualizar(id, comentarioAtualizado);
        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Comentario existente = comentarioService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        comentarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<Comentario>> listarComentariosSala(@PathVariable Long salaId) {
        System.out.println("Buscando comentários da sala: " + salaId);
        List<Comentario> comentarios = comentarioService.listarComentariosPorSala(salaId);
        List<Comentario> filtrados = comentarios.stream()
                .filter(c -> c.getTipoDestino() == TipoDestinoComentario.SALADEAULA)
                .collect(Collectors.toList());
        System.out.println("Encontrados " + filtrados.size() + " comentários da sala");
        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/sala/{salaId}/atividades-gerais")
    public ResponseEntity<List<Comentario>> listarComentariosAtividadesGerais(@PathVariable Long salaId) {
        System.out.println("Buscando comentários de atividades gerais da sala: " + salaId);
        List<Comentario> comentarios = comentarioService.listarComentariosPorSala(salaId);
        List<Comentario> filtrados = comentarios.stream()
                .filter(c -> c.getTipoDestino() == TipoDestinoComentario.ATIVIDADES_GERAIS)
                .collect(Collectors.toList());
        System.out.println("Encontrados " + filtrados.size() + " comentários de atividades gerais");
        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/atividade/{atividadeId}")
    public ResponseEntity<List<Comentario>> listarComentariosAtividade(@PathVariable Long atividadeId) {
        List<Comentario> comentarios = comentarioService.listarComentariosPorAtividade(atividadeId);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/postagem/{postagemId}")
    public ResponseEntity<List<Comentario>> listarComentariosPostagem(@PathVariable Long postagemId) {
        System.out.println("[GET /comentario/postagem/" + postagemId + "] Buscando comentários");
        List<Comentario> comentarios = comentarioService.listarComentariosPorPostagem(postagemId);
        System.out.println("[GET /comentario/postagem/" + postagemId + "] Total: " + comentarios.size());
        return ResponseEntity.ok(comentarios);
    }
}
