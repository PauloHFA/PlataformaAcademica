package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.UsuarioDTO;
import com.plataforma_academica.plataforma.dto.UsuarioResponseDTO;
import com.plataforma_academica.plataforma.model.Usuario;

import java.util.Base64;

public class UsuarioMapper {

    // ============================================================
    // ENTITY → DTO (simples, usado em requests de criação/edição)
    // ============================================================
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());

        if (usuario.getAvatar() != null) {
            dto.setAvatarBase64(Base64.getEncoder().encodeToString(usuario.getAvatar()));
        }

        return dto;
    }

    // ============================================================
    // ENTITY → RESPONSE DTO (para exibição no front)
    // ============================================================
    public static UsuarioResponseDTO toResponse(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponseDTO resp = new UsuarioResponseDTO();

        resp.setId(usuario.getId());
        resp.setNome(usuario.getNome());
        resp.setEmail(usuario.getEmail());

        // Aqui você define sua URL de arquivo, caso esteja servindo direto do backend
        if (usuario.getAvatar() != null) {
            // Exibe a imagem em formato Base64 (opcional)
            resp.setAvatarBase64(Base64.getEncoder().encodeToString(usuario.getAvatar()));

            // Se você possui um endpoint para servir imagens, pode montar a URL:
            resp.setAvatarUrl("/api/usuarios/" + usuario.getId() + "/avatar");
        }

        return resp;
    }

    // ============================================================
    // DTO → ENTITY (para salvar no banco)
    // ============================================================
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();

        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        if (dto.getAvatarBase64() != null) {
            usuario.setAvatar(Base64.getDecoder().decode(dto.getAvatarBase64()));
        }

        return usuario;
    }
}
