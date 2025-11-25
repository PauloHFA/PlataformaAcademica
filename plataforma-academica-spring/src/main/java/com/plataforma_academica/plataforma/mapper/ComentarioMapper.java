package com.plataforma_academica.plataforma.mapper;

import com.plataforma_academica.plataforma.dto.ComentarioDTO;
import com.plataforma_academica.plataforma.dto.ComentarioResponseDTO;
import com.plataforma_academica.plataforma.model.*;

public class ComentarioMapper {

    // ----------------------------------------------------
    // ENTITY → RESPONSE DTO
    // ----------------------------------------------------
    public static ComentarioResponseDTO toResponse(Comentario comentario) {
        if (comentario == null) return null;

        ComentarioResponseDTO resp = new ComentarioResponseDTO();

        resp.setId(comentario.getId());
        resp.setConteudo(comentario.getConteudo());
        resp.setDataCriacao(comentario.getDataCriacao());
        resp.setTipoDestino(comentario.getTipoDestino().name());

        // Autor
        if (comentario.getAutor() != null) {
            resp.setAutorId(comentario.getAutor().getId());
            resp.setAutorNome(comentario.getAutor().getNome());
        }

        // Apenas um destino será preenchido
        switch (comentario.getTipoDestino()) {

            case POSTAGEM:
                if (comentario.getPostagem() != null) {
                    resp.setDestinoId(comentario.getPostagem().getId());
                }
                break;

            case ATIVIDADE:
                if (comentario.getAtividade() != null) {
                    resp.setDestinoId(comentario.getAtividade().getId());
                }
                break;

            case SALADEAULA:
                if (comentario.getSaladeAula() != null) {
                    resp.setDestinoId(comentario.getSaladeAula().getId());
                }
                break;
        }

        return resp;
    }

    // ----------------------------------------------------
    // ENTITY → DTO (caso ainda seja usado internamente)
    // ----------------------------------------------------
    public static ComentarioDTO toDTO(Comentario comentario) {
        if (comentario == null) return null;

        ComentarioDTO dto = new ComentarioDTO();

        dto.setId(comentario.getId());
        dto.setConteudo(comentario.getConteudo());
        dto.setDataCriacao(comentario.getDataCriacao());
        dto.setTipoDestino(comentario.getTipoDestino().name());

        dto.setAutorId(comentario.getAutor() != null ? comentario.getAutor().getId() : null);
        dto.setPostagemId(comentario.getPostagem() != null ? comentario.getPostagem().getId() : null);
        dto.setAtividadeId(comentario.getAtividade() != null ? comentario.getAtividade().getId() : null);
        dto.setSalaId(comentario.getSaladeAula() != null ? comentario.getSaladeAula().getId(): null);

        return dto;
    }

    // ----------------------------------------------------
    // DTO → ENTITY
    // Service deve carregar entidades reais
    // ----------------------------------------------------
    public static Comentario toEntity(
            ComentarioDTO dto,
            Usuario autor,
            Postagem postagem,
            Atividade atividade,
            SaladeAula sala,
            TipoDestinoComentario tipoDestino
    ) {
        if (dto == null) return null;

        Comentario comentario = new Comentario();

        comentario.setId(dto.getId());
        comentario.setConteudo(dto.getConteudo());
        comentario.setDataCriacao(dto.getDataCriacao());

        comentario.setAutor(autor);
        comentario.setPostagem(postagem);
        comentario.setAtividade(atividade);
        comentario.setSaladeAula(sala);
        comentario.setTipoDestino(tipoDestino);

        return comentario;
    }
}
