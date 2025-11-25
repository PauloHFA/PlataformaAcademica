package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Override
    public Comentario salvar(Comentario comentario) {
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
}
