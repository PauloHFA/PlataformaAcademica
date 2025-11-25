package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.model.Artigo;

import java.util.List;

public interface ArtigoService {
    Artigo criar(ArtigoDTO artigoDTO);
    Artigo editar(Long id, ArtigoDTO dto);
    void deletar(Long id, Long solicitanteId);
    Artigo buscarPorId(Long id);
    List<Artigo> listarTodos();
    List<Artigo> listarPorAutor(Long autorId);
}
