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

        // Como Perfil herda de Usuario, os campos estão diretamente no perfil
        resp.setUsuarioId(perfil.getId());
        resp.setUsuarioNome(perfil.getNome());

        return resp;
    }

    // ----------------------------------------------------
    // ENTITY → DTO  (caso utilize internamente)
    // ----------------------------------------------------
    public static PerfilDTO toDTO(Perfil perfil) {
        if (perfil == null) return null;

        PerfilDTO dto = new PerfilDTO();

        dto.setId(perfil.getId());
        dto.setNome(perfil.getNome());
        dto.setSobrenome(perfil.getSobrenome());
        dto.setEmail(perfil.getEmail());
        dto.setInstituicaoEnsino(perfil.getInstituicaoEnsino());
        dto.setCep(perfil.getCep());
        dto.setPais(perfil.getPais());
        dto.setCidade(perfil.getCidade());
        dto.setSite(perfil.getSite());
        dto.setTelefone(perfil.getTelefone());
        dto.setDataNascimento(perfil.getDataNascimento() != null ? perfil.getDataNascimento().toString() : null);
        dto.setDescricao(perfil.getDescricao());
        dto.setBio(perfil.getBio());
        dto.setFotoPerfil(perfil.getFotoPerfil());
        dto.setCurso(perfil.getCurso());
        dto.setUsuarioId(perfil.getId()); // mesmo id

        return dto;
    }

    // ----------------------------------------------------
    // DTO → ENTITY (usado na criação/edição)
    // ----------------------------------------------------
    public static Perfil toEntity(PerfilDTO dto) {
        if (dto == null) return null;

        Perfil perfil = new Perfil();

        perfil.setNome(dto.getNome());
        perfil.setSobrenome(dto.getSobrenome());
        perfil.setEmail(dto.getEmail());
        perfil.setInstituicaoEnsino(dto.getInstituicaoEnsino());
        perfil.setCep(dto.getCep());
        perfil.setPais(dto.getPais());
        perfil.setCidade(dto.getCidade());
        perfil.setSite(dto.getSite());
        perfil.setTelefone(dto.getTelefone());
        if (dto.getDataNascimento() != null) {
            perfil.setDataNascimento(java.time.LocalDate.parse(dto.getDataNascimento()));
        }
        perfil.setDescricao(dto.getDescricao());
        perfil.setBio(dto.getBio());
        perfil.setFotoPerfil(dto.getFotoPerfil());
        perfil.setCurso(dto.getCurso());

        return perfil;
    }
}
