package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;

import java.util.List;

public interface PostagemService {
    PostagemDTO publicar(PostagemDTO postagemDTO);
    List<PostagemDTO> listarTodas();
    PostagemDTO buscarPorId(Long id);
    List<PostagemDTO> buscarPorTitulo(String titulo);
    PostagemDTO atualizar(PostagemDTO postagemDTO);
    void deletar(Long id);
    
    // Response methods
    PostagemResponseDTO publicarResponse(PostagemDTO postagemDTO);
    List<PostagemResponseDTO> listarTodasResponse();
    PostagemResponseDTO buscarPorIdResponse(Long id);
    List<PostagemResponseDTO> buscarPorTituloResponse(String titulo);
    PostagemResponseDTO atualizarResponse(PostagemDTO postagemDTO);
    
    PostagemResponseDTO curtir(Long id);
    List<PostagemResponseDTO> listarDeAmigos(Long usuarioId);
    List<PostagemResponseDTO> listarMaisCurtidas();
}
