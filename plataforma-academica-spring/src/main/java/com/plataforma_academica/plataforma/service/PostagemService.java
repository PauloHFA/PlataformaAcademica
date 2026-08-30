package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;

import java.util.List;

/**
 * Interface do serviço de Postagens.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Postagem
 * @see REQ-025 (Publicação no Feed Social)
 */
/**
 * Interface do serviço de Postagens.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see Postagem
 * @see REQ-025 (Publicação no Feed Social)
 */
public interface PostagemService {
    PostagemDTO publicar(PostagemDTO postagemDTO);

    List<PostagemDTO> listarTodas();

    PostagemDTO buscarPorId(UUID id);

    List<PostagemDTO> buscarPorTitulo(String titulo);

    PostagemDTO atualizar(PostagemDTO postagemDTO);

    void deletar(UUID id);

    // Response methods
    PostagemResponseDTO publicarResponse(PostagemDTO postagemDTO);

    List<PostagemResponseDTO> listarTodasResponse();

    PostagemResponseDTO buscarPorIdResponse(UUID id);

    List<PostagemResponseDTO> buscarPorTituloResponse(String titulo);

    PostagemResponseDTO atualizarResponse(PostagemDTO postagemDTO);

    PostagemResponseDTO curtir(UUID postagemId, UUID usuarioId);

    List<PostagemResponseDTO> listarDeAmigos(UUID usuarioId);

    List<PostagemResponseDTO> listarMaisCurtidas();

    boolean verificarCurtida(UUID postagemId, UUID usuarioId);

    // Suporta criação com upload de imagem (multipart)
    PostagemResponseDTO publicarComImagemResponse(PostagemDTO postagemDTO,
            org.springframework.web.multipart.MultipartFile imagem);
}
