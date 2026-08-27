package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.dto.ComunidadeResponseDTO;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.Usuario;

/**
 * Mapper para conversão entre Comunidade e DTOs.
 * 
 * Camada: Infrastructure / Mapper
 * Padrões aplicados: Static Mapping Pattern, DTO.
 * 
 * @see Comunidade
 * @see ComunidadeDTO
 */
public class ComunidadeMapper {

    // ----------------------------------------------------
    // ENTITY → RESPONSE DTO (retorno ao front)
    // ----------------------------------------------------
    public static ComunidadeResponseDTO toResponse(Comunidade comunidade) {
        if (comunidade == null)
            return null;

        ComunidadeResponseDTO resp = new ComunidadeResponseDTO();

        resp.setId(comunidade.getId());
        resp.setNome(comunidade.getNome());
        resp.setDescricao(comunidade.getDescricao());
        resp.setCriadoEm(comunidade.getCriadoEm());

        // Dono
        if (comunidade.getDono() != null) {
            resp.setDonoId(comunidade.getDono().getId());
            resp.setDonoNome(comunidade.getDono().getNome());
        }

        return resp;
    }

    // ----------------------------------------------------
    // ENTITY → DTO (interno, caso necessário)
    // ----------------------------------------------------
    public static ComunidadeDTO toDTO(Comunidade comunidade) {
        if (comunidade == null)
            return null;

        ComunidadeDTO dto = new ComunidadeDTO();

        dto.setId(comunidade.getId());
        dto.setNome(comunidade.getNome());
        dto.setDescricao(comunidade.getDescricao());
        dto.setCriadoEm(comunidade.getCriadoEm());
        dto.setDonoId(comunidade.getDono() != null ? comunidade.getDono().getId() : null);

        return dto;
    }

    // ----------------------------------------------------
    // DTO → ENTITY
    // ----------------------------------------------------
    public static Comunidade toEntity(ComunidadeDTO dto, Usuario dono) {
        if (dto == null)
            return null;

        Comunidade comunidade = new Comunidade();

        comunidade.setId(dto.getId());
        comunidade.setNome(dto.getNome());
        comunidade.setDescricao(dto.getDescricao());
        comunidade.setCriadoEm(dto.getCriadoEm());
        comunidade.setDono(dono);

        return comunidade;
    }
}
