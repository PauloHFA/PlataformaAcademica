package com.plataforma_academica.plataforma.service;

import java.util.UUID;

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

    Comentario buscarPorId(UUID id);

    List<Comentario> listarTodos();

    Comentario atualizar(UUID id, Comentario comentarioAtualizado);

    void deletar(UUID id);

    List<Comentario> listarComentariosPorSala(UUID salaId);

    List<Comentario> listarComentariosPorAtividade(UUID atividadeId);

    List<Comentario> listarComentariosPorPostagem(UUID postagemId);
}
