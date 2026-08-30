package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;

import java.util.List;

/**
 * Interface do serviço de Perfis acadêmicos.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Perfil
 * @see REQ-002 (Perfil Acadêmico)
 */
public interface PerfilService {

    Perfil salvar(PerfilDTO dto);

    Perfil atualizar(UUID id, PerfilDTO dto);

    List<Perfil> listarTodos();

    Perfil buscarPorId(UUID id);

    List<Perfil> buscarPorCurso(String curso);

    Perfil buscarPorUsuarioId(UUID usuarioId);

    boolean existePerfilDoUsuario(UUID usuarioId);
}
