package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.RecomendacaoUsuarioDTO;
import com.plataforma_academica.plataforma.model.InteracaoUsuario;
import com.plataforma_academica.plataforma.service.RecomendacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pela API de Recomendações de Usuários.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Social / Sistema de Recomendação por similaridade.
 * Padrões aplicados: RestController, RequiredArgsConstructor.
 * 
 * @see RecomendacaoService
 * @see REQ-042 (Sistema de Recomendação por Similaridade)
 */
@RestController
@RequestMapping("/api/recomendacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    /**
     * Gera novas recomendações para um usuário
     */
    @PostMapping("/gerar/{usuarioId}")
    public ResponseEntity<List<RecomendacaoUsuarioDTO>> gerarRecomendacoes(@PathVariable Long usuarioId) {
        try {
            List<RecomendacaoUsuarioDTO> recomendacoes = recomendacaoService.gerarRecomendacoes(usuarioId);
            return ResponseEntity.ok(recomendacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca recomendações existentes para um usuário
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<RecomendacaoUsuarioDTO>> buscarRecomendacoes(@PathVariable Long usuarioId) {
        try {
            List<RecomendacaoUsuarioDTO> recomendacoes = recomendacaoService.buscarRecomendacoes(usuarioId);
            return ResponseEntity.ok(recomendacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Registra uma interação do usuário para melhorar recomendações
     */
    @PostMapping("/interacao")
    public ResponseEntity<Void> registrarInteracao(
            @RequestParam Long usuarioId,
            @RequestParam InteracaoUsuario.TipoInteracao tipoInteracao,
            @RequestParam String entidadeTipo,
            @RequestParam Long entidadeId,
            @RequestParam(required = false) String tags) {

        try {
            recomendacaoService.registrarInteracao(usuarioId, tipoInteracao, entidadeTipo, entidadeId, tags);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para integração automática - registra interações comuns
     */
    @PostMapping("/auto-registrar")
    public ResponseEntity<Void> autoRegistrarInteracao(
            @RequestParam Long usuarioId,
            @RequestParam String acao,
            @RequestParam Long entidadeId) {

        try {
            InteracaoUsuario.TipoInteracao tipo = mapearAcaoParaTipo(acao);
            String entidadeTipo = determinarEntidadeTipo(acao);

            recomendacaoService.registrarInteracao(usuarioId, tipo, entidadeTipo, entidadeId, null);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private InteracaoUsuario.TipoInteracao mapearAcaoParaTipo(String acao) {
        switch (acao.toLowerCase()) {
            case "curtir":
            case "like":
                return InteracaoUsuario.TipoInteracao.CURTIDA;
            case "comentar":
            case "comment":
                return InteracaoUsuario.TipoInteracao.COMENTARIO;
            case "compartilhar":
            case "share":
                return InteracaoUsuario.TipoInteracao.COMPARTILHAMENTO;
            case "visualizar":
            case "view":
                return InteracaoUsuario.TipoInteracao.VISUALIZACAO;
            case "participar_atividade":
                return InteracaoUsuario.TipoInteracao.PARTICIPACAO_ATIVIDADE;
            case "entrar_comunidade":
                return InteracaoUsuario.TipoInteracao.ENTRADA_COMUNIDADE;
            case "solicitar_amizade":
                return InteracaoUsuario.TipoInteracao.ENVIO_SOLICITACAO_AMIZADE;
            case "aceitar_amizade":
                return InteracaoUsuario.TipoInteracao.ACEITACAO_AMIZADE;
            default:
                return InteracaoUsuario.TipoInteracao.VISUALIZACAO;
        }
    }

    private String determinarEntidadeTipo(String acao) {
        if (acao.contains("post") || acao.contains("postagem")) {
            return "POSTAGEM";
        } else if (acao.contains("atividade")) {
            return "ATIVIDADE";
        } else if (acao.contains("comunidade")) {
            return "COMUNIDADE";
        } else if (acao.contains("usuario") || acao.contains("amizade")) {
            return "USUARIO";
        }
        return "ENTIDADE";
    }
}