package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.dto.AtividadeResponseDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;

public class AtividadeMapper {

    // ======================================================
    // DTO → ENTIDADE  (entrada: criação / edição)
    // ======================================================
    public static Atividade toEntity(AtividadeDTO dto, Usuario autor, SaladeAula sala) {
        if (dto == null) return null;

        Atividade atividade = new Atividade();

        atividade.setId(dto.getId());
        atividade.setTitulo(dto.getTitulo());
        atividade.setDescricao(dto.getDescricao());
        atividade.setTipoDocumentoSubmissao(dto.getTipoDocumentoSubmissao());
        if (dto.getDataEntrega() != null) {
            String dataStr = dto.getDataEntrega();
            if (dataStr.contains("T")) {
                atividade.setDataEntrega(java.time.LocalDate.parse(dataStr.substring(0, 10)));
            } else {
                atividade.setDataEntrega(java.time.LocalDate.parse(dataStr));
            }
        }
        atividade.setPontos(dto.getPontos());

        atividade.setAutor(autor);      // entidade carregada no service
        atividade.setSalaDeAula(sala);  // entidade carregada no service

        return atividade;
    }

    // ======================================================
    // ENTIDADE → RESPONSE DTO (saída para o cliente)
    // ======================================================
    public static AtividadeResponseDTO toResponse(Atividade entidade) {
        if (entidade == null) return null;

        AtividadeResponseDTO response = new AtividadeResponseDTO();

        response.setId(entidade.getId());
        response.setTitulo(entidade.getTitulo());
        response.setDescricao(entidade.getDescricao());
        response.setTipoDocumentoSubmissao(entidade.getTipoDocumentoSubmissao());
        if (entidade.getDataEntrega() != null) {
            response.setDataEntrega(entidade.getDataEntrega().toString());
        }
        response.setPontos(entidade.getPontos());
        response.setDocumentoUrl(entidade.getDocumentoUrl());

        if (entidade.getAutor() != null) {
            response.setAutorId(entidade.getAutor().getId());
            response.setAutorNome(entidade.getAutor().getNome());
        }

        if (entidade.getSalaDeAula() != null) {
            response.setSalaId(entidade.getSalaDeAula().getId());
            response.setSalaNome(entidade.getSalaDeAula().getNome());
        }

        return response;
    }

    // ======================================================
    // Atualizar entidade existente (edição)
    // ======================================================
    public static void updateEntity(Atividade entidade, AtividadeDTO dto) {
        entidade.setTitulo(dto.getTitulo());
        entidade.setDescricao(dto.getDescricao());
        entidade.setTipoDocumentoSubmissao(dto.getTipoDocumentoSubmissao());
        if (dto.getDataEntrega() != null) {
            String dataStr = dto.getDataEntrega();
            if (dataStr.contains("T")) {
                entidade.setDataEntrega(java.time.LocalDate.parse(dataStr.substring(0, 10)));
            } else {
                entidade.setDataEntrega(java.time.LocalDate.parse(dataStr));
            }
        }
        entidade.setPontos(dto.getPontos());
    }
}
