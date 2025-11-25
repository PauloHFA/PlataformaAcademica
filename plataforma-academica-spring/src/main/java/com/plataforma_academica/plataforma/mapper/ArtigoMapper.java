package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.dto.ArtigoResponseDTO;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.model.Usuario;

public class ArtigoMapper {

    // ============================================
    // DTO → ENTIDADE (criação e edição)
    // ============================================
    public static Artigo toEntity(ArtigoDTO dto, Usuario autor) {
        if (dto == null) return null;

        Artigo artigo = new Artigo();

        artigo.setId(dto.getId());
        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());

        artigo.setAutor(autor);

        // timestamps: geralmente são definidos no serviço
        artigo.setCriadoEm(dto.getCriadoEm());
        artigo.setAtualizadoEm(dto.getAtualizadoEm());

        return artigo;
    }

    // ============================================
    // ENTIDADE → RESPONSE DTO
    // ============================================
    public static ArtigoResponseDTO toResponse(Artigo artigo) {
        if (artigo == null) return null;

        ArtigoResponseDTO response = new ArtigoResponseDTO();

        response.setId(artigo.getId());
        response.setTitulo(artigo.getTitulo());
        response.setConteudo(artigo.getConteudo());
        response.setCriadoEm(artigo.getCriadoEm());
        response.setAtualizadoEm(artigo.getAtualizadoEm());

        if (artigo.getAutor() != null) {
            response.setAutorId(artigo.getAutor().getId());
            response.setAutorNome(artigo.getAutor().getNome());
        }

        return response;
    }

    // ============================================
    // Atualizar entidade existente
    // ============================================
    public static void updateEntity(Artigo artigo, ArtigoDTO dto) {
        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());
        artigo.setAtualizadoEm(dto.getAtualizadoEm());
    }
}
