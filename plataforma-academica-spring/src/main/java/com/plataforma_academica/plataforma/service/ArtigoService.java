package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.model.Artigo;

import java.util.List;

public interface ArtigoService {
    Artigo criar(ArtigoDTO artigoDTO);
    Artigo editar(UUID id, ArtigoDTO dto);
    void deletar(UUID id, UUID solicitanteId);
    Artigo buscarPorId(UUID id);
    List<Artigo> listarTodos();
    List<Artigo> listarPorAutor(UUID autorId);
}
