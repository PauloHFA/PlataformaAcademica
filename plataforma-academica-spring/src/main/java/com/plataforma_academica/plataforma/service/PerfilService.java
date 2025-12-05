package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;

import java.util.List;

public interface PerfilService {

    Perfil salvar(PerfilDTO dto);

    Perfil atualizar(Long id, PerfilDTO dto);

    List<Perfil> listarTodos();

    Perfil buscarPorId(Long id);

    List<Perfil> buscarPorCurso(String curso);

    Perfil buscarPorUsuarioId(Long usuarioId);

    boolean existePerfilDoUsuario(Long usuarioId);
}
