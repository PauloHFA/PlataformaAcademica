package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ComentarioDTO;
import com.plataforma_academica.plataforma.model.Comentario;

import java.util.List;

public interface ComentarioService {

    Comentario salvar(Comentario comentario);

    Comentario salvarComentario(ComentarioDTO dto);

    Comentario buscarPorId(Long id);

    List<Comentario> listarTodos();

    Comentario atualizar(Long id, Comentario comentarioAtualizado);

    void deletar(Long id);

    List<Comentario> listarComentariosPorSala(Long salaId);

    List<Comentario> listarComentariosPorAtividade(Long atividadeId);

    List<Comentario> listarComentariosPorPostagem(Long postagemId);
}
