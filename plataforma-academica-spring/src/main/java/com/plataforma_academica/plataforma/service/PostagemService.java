package com.plataforma_academica.plataforma.service;

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

    PostagemResponseDTO curtir(Long postagemId, Long usuarioId);

    List<PostagemResponseDTO> listarDeAmigos(Long usuarioId);

    List<PostagemResponseDTO> listarMaisCurtidas();

    boolean verificarCurtida(Long postagemId, Long usuarioId);

    // Suporta criação com upload de imagem (multipart)
    PostagemResponseDTO publicarComImagemResponse(PostagemDTO postagemDTO,
            org.springframework.web.multipart.MultipartFile imagem);
}
