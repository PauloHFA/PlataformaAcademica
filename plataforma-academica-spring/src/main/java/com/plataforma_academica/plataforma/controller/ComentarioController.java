package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.TipoDestinoComentario;
import com.plataforma_academica.plataforma.service.ComentarioService;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comentario")
@CrossOrigin(origins = "http://localhost:4200")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;
    
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
        System.out.println("Recebido comentário: " + comentario.getConteudo());
        System.out.println("Tipo destino: " + comentario.getTipoDestino());
        
        if (comentario.getSaladeAula() != null && comentario.getSaladeAula().getId() != null) {
            SaladeAula sala = salaRepository.findById(comentario.getSaladeAula().getId()).orElseThrow(() -> new RuntimeException("Sala não encontrada"));
            comentario.setSaladeAula(sala);
        }
        
        if (comentario.getAtividade() != null && comentario.getAtividade().getId() != null) {
            Atividade atividade = atividadeRepository.findById(comentario.getAtividade().getId()).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
            comentario.setAtividade(atividade);
        }
        
        if (comentario.getPostagem() != null && comentario.getPostagem().getId() != null) {
            com.plataforma_academica.plataforma.model.Postagem postagem = postagemRepository.findById(comentario.getPostagem().getId()).orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
            comentario.setPostagem(postagem);
        }
        
        Comentario salvo = comentarioService.salvar(comentario);
        URI location = URI.create("/comentario/" + salvo.getId());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> atualizar(
            @PathVariable Long id,
            @RequestBody Comentario comentarioAtualizado
    ) {
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
        List<Comentario> comentarios = comentarioService.listarComentariosPorPostagem(postagemId);
        return ResponseEntity.ok(comentarios);
    }
}
