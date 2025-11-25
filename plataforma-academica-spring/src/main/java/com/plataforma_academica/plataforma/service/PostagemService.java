package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PostagemDTO;

import java.util.List;

public interface PostagemService {
    PostagemDTO publicar(PostagemDTO postagemDTO);
    List<PostagemDTO> listarTodas();
    PostagemDTO buscarPorId(Long id);
    List<PostagemDTO> buscarPorTitulo(String titulo);
    PostagemDTO atualizar(PostagemDTO postagemDTO);
    void deletar(Long id);
}
