package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ComentarioDTO;
import com.plataforma_academica.plataforma.model.Comentario;

import java.util.List;

/**
 * Interface do serviço de Comentários.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Comentario
 * @see REQ-030 (Sistema de Comentários)
 */
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
