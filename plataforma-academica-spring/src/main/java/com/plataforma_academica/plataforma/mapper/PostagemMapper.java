package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;
import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.model.Postagem;
import com.plataforma_academica.plataforma.model.Usuario;

public class PostagemMapper {

    // ==================================================
    // ENTITY → DTO (para criar/atualizar)
    // ==================================================
    public static PostagemDTO toDTO(Postagem postagem) {
        if (postagem == null) return null;

        PostagemDTO dto = new PostagemDTO();

        dto.setId(postagem.getId());
        dto.setTitulo(postagem.getTitulo());
        dto.setConteudo(postagem.getConteudo());

        dto.setAutorId(
                postagem.getAutor() != null ? postagem.getAutor().getId() : null
        );

        dto.setPlataformaId(
                postagem.getPlataforma() != null ? postagem.getPlataforma().getId() : null
        );

        return dto;
    }

    // ==================================================
    // ENTITY → RESPONSE DTO (para retornar ao front)
    // ==================================================
    public static PostagemResponseDTO toResponse(Postagem postagem) {
        if (postagem == null) return null;

        PostagemResponseDTO response = new PostagemResponseDTO();

        response.setId(postagem.getId());
        response.setTitulo(postagem.getTitulo());
        response.setConteudo(postagem.getConteudo());

        if (postagem.getAutor() != null) {
            response.setAutorId(postagem.getAutor().getId());
            response.setAutorNome(postagem.getAutor().getNome());
        }

        if (postagem.getPlataforma() != null) {
            response.setPlataformaId(postagem.getPlataforma().getId());
            response.setPlataformaNome(postagem.getPlataforma().getNome());
        }

        return response;
    }

    // ==================================================
    // DTO → ENTITY (para salvar no banco)
    // ==================================================
    public static Postagem toEntity(PostagemDTO dto, Usuario autor, Plataforma plataforma) {
        if (dto == null) return null;

        Postagem postagem = new Postagem();

        postagem.setId(dto.getId());
        postagem.setTitulo(dto.getTitulo());
        postagem.setConteudo(dto.getConteudo());
        postagem.setAutor(autor);
        postagem.setPlataforma(plataforma);

        return postagem;
    }
}
