package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.SalaDeAulaDTO;
import com.plataforma_academica.plataforma.dto.SalaDeAulaResponseDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre SaladeAula e DTOs.
 * 
 * Camada: Infrastructure / Mapper
 * Padrões aplicados: Static Mapping Pattern, DTO.
 * 
 * @see SaladeAula
 * @see SalaDeAulaDTO
 */
public class SalaDeAulaMapper {

    // ============================================================
    // ENTITY → DTO (para criação/edição - dados básicos)
    // ============================================================
    public static SalaDeAulaDTO toDTO(SaladeAula sala) {
        if (sala == null)
            return null;

        SalaDeAulaDTO dto = new SalaDeAulaDTO();

        dto.setId(sala.getId());
        dto.setNome(sala.getNome());

        // Criador
        dto.setCriadorId(
                sala.getCriador() != null ? sala.getCriador().getId() : null);

        // Membros
        dto.setMembrosIds(
                sala.getUsuarios() != null
                        ? sala.getUsuarios().stream()
                                .map(Usuario::getId)
                                .collect(Collectors.toList())
                        : List.of());

        // Atividades
        dto.setAtividadesIds(
                sala.getAtividades() != null
                        ? sala.getAtividades().stream()
                                .map(Atividade::getId)
                                .collect(Collectors.toList())
                        : List.of());

        return dto;
    }

    // ============================================================
    // ENTITY → RESPONSE DTO (para retorno completo ao front)
    // ============================================================
    public static SalaDeAulaResponseDTO toResponse(SaladeAula sala) {
        if (sala == null)
            return null;

        SalaDeAulaResponseDTO response = new SalaDeAulaResponseDTO();

        response.setId(sala.getId());
        response.setNome(sala.getNome());

        // Criador
        if (sala.getCriador() != null) {
            response.setCriadorId(sala.getCriador().getId());
            response.setCriadorNome(sala.getCriador().getNome());
        }

        // Membros
        if (sala.getUsuarios() != null) {
            response.setMembrosIds(
                    sala.getUsuarios().stream()
                            .map(Usuario::getId)
                            .collect(Collectors.toList()));

            response.setMembrosNomes(
                    sala.getUsuarios().stream()
                            .map(Usuario::getNome)
                            .collect(Collectors.toList()));
        }

        // Atividades
        if (sala.getAtividades() != null) {
            response.setAtividadesIds(
                    sala.getAtividades().stream()
                            .map(Atividade::getId)
                            .collect(Collectors.toList()));
        }

        return response;
    }

    // ============================================================
    // DTO → ENTITY (para salvar no banco)
    // ============================================================
    public static SaladeAula toEntity(
            SalaDeAulaDTO dto,
            Usuario criador,
            List<Usuario> membros,
            List<Atividade> atividades) {
        if (dto == null)
            return null;

        SaladeAula sala = new SaladeAula();

        sala.setId(dto.getId());
        sala.setNome(dto.getNome());
        sala.setCriador(criador);
        sala.setUsuarios(membros);
        sala.setAtividades(atividades);

        return sala;
    }
}
