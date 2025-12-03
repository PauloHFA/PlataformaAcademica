package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public Comentario salvar(Comentario comentario) {
        if (comentario.getDataCriacao() == null) {
            comentario.setDataCriacao(java.time.LocalDateTime.now());
        }
        System.out.println("Salvando comentário: " + comentario.getConteudo());
        System.out.println("Sala: " + (comentario.getSaladeAula() != null ? comentario.getSaladeAula().getId() : "null"));
        System.out.println("Tipo destino: " + comentario.getTipoDestino());
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }

    @Override
    public Comentario atualizar(Long id, Comentario comentarioAtualizado) {
        Comentario existente = buscarPorId(id);

        if (existente == null) {
            return null;
        }

        existente.setConteudo(comentarioAtualizado.getConteudo());
        existente.setAutor(comentarioAtualizado.getAutor());
        existente.setPostagem(comentarioAtualizado.getPostagem());

        return comentarioRepository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public List<Comentario> listarComentariosPorSala(Long salaId) {
        return comentarioRepository.findBySaladeAulaId(salaId);
    }

    @Override
    public List<Comentario> listarComentariosPorAtividade(Long atividadeId) {
        return comentarioRepository.findByAtividadeId(atividadeId);
    }

    @Override
    public List<Comentario> listarComentariosPorPostagem(Long postagemId) {
        return comentarioRepository.findByPostagemId(postagemId);
    }
    
    public Comentario salvarComentarioSala(Comentario comentario) {
        if (comentario.getSaladeAula() == null || comentario.getSaladeAula().getId() == null) {
            throw new IllegalArgumentException("Sala de aula é obrigatória");
        }
        if (comentario.getAutor() == null || comentario.getAutor().getId() == null) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        comentario.setTipoDestino(com.plataforma_academica.plataforma.model.TipoDestinoComentario.SALADEAULA);
        return this.salvar(comentario);
    }
}
