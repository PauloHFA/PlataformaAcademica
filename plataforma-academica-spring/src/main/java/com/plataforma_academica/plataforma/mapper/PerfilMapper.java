package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.dto.PerfilResponseDTO;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.model.Usuario;

public class PerfilMapper {

    // ----------------------------------------------------
    // ENTITY → RESPONSE (retornado ao frontend)
    // ----------------------------------------------------
    public static PerfilResponseDTO toResponse(Perfil perfil) {
        if (perfil == null) return null;

        PerfilResponseDTO resp = new PerfilResponseDTO();

        resp.setId(perfil.getId());
        resp.setBio(perfil.getBio());
        resp.setFotoPerfil(perfil.getFotoPerfil());
        resp.setCurso(perfil.getCurso());

        if (perfil.getUsuario() != null) {
            resp.setUsuarioId(perfil.getUsuario().getId());
            resp.setUsuarioNome(perfil.getUsuario().getNome());
        }

        return resp;
    }

    // ----------------------------------------------------
    // ENTITY → DTO  (caso utilize internamente)
    // ----------------------------------------------------
    public static PerfilDTO toDTO(Perfil perfil) {
        if (perfil == null) return null;

        PerfilDTO dto = new PerfilDTO();

        dto.setId(perfil.getId());
        dto.setBio(perfil.getBio());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setCurso(perfil.getCurso());
        dto.setUsuarioId(perfil.getUsuario() != null ? perfil.getUsuario().getId() : null);

        return dto;
    }

    // ----------------------------------------------------
    // DTO → ENTITY (usado na criação/edição)
    // ----------------------------------------------------
    public static Perfil toEntity(PerfilDTO dto, Usuario usuario) {
        if (dto == null) return null;

        Perfil perfil = new Perfil();

        perfil.setId(dto.getId());
        perfil.setBio(dto.getBio());
        perfil.setFotoPerfil(dto.getFotoPerfil());
        perfil.setCurso(dto.getCurso());
        perfil.setUsuario(usuario); // entidade carregada antes no service

        return perfil;
    }
}
