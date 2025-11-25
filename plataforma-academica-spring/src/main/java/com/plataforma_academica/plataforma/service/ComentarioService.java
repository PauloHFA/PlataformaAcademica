package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Comentario;

import java.util.List;

public interface ComentarioService {

    Comentario salvar(Comentario comentario);

    Comentario buscarPorId(Long id);

    List<Comentario> listarTodos();

    Comentario atualizar(Long id, Comentario comentarioAtualizado);

    void deletar(Long id);
}
