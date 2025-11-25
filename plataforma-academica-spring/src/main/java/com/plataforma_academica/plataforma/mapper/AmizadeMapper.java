package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.dto.AmizadeResponseDTO;
import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;

public class AmizadeMapper {

    // ============================================
    // DTO → ENTIDADE (criação e atualização)
    // ============================================
    public static Amizade toEntity(AmizadeDTO dto, Usuario solicitante, Usuario destinatario) {
        if (dto == null) return null;

        Amizade amizade = new Amizade();

        amizade.setId(dto.getId());
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);

        if (dto.getStatus() != null) {
            amizade.setStatus(Amizade.Status.valueOf(dto.getStatus()));
        }

        amizade.setCriadoEm(dto.getCriadoEm());

        return amizade;
    }

    // ============================================
    // ENTIDADE → RESPONSE DTO
    // ============================================
    public static AmizadeResponseDTO toResponse(Amizade amizade) {
        if (amizade == null) return null;

        AmizadeResponseDTO response = new AmizadeResponseDTO();

        response.setId(amizade.getId());

        // Solicitante
        if (amizade.getSolicitante() != null) {
            response.setSolicitanteId(amizade.getSolicitante().getId());
            response.setSolicitanteNome(amizade.getSolicitante().getNome());
        }

        // Destinatário
        if (amizade.getDestinatario() != null) {
            response.setDestinatarioId(amizade.getDestinatario().getId());
            response.setDestinatarioNome(amizade.getDestinatario().getNome());
        }

        response.setStatus(amizade.getStatus() != null ? amizade.getStatus().name() : null);

        response.setCriadoEm(amizade.getCriadoEm());

        return response;
    }

    // ============================================
    // Atualizar entidade existente (geralmente só status)
    // ============================================
    public static void updateEntity(Amizade amizade, AmizadeDTO dto) {
        if (dto.getStatus() != null) {
            amizade.setStatus(Amizade.Status.valueOf(dto.getStatus()));
        }
    }
}
