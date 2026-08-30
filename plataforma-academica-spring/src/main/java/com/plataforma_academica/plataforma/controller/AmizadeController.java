package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.dto.AmizadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.AmizadeMapper;
import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.service.AmizadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * ==========================================================================================================
 *  AMIZADE CONTROLLER — Plataforma Acadêmica
 * ==========================================================================================================
 *
 * Estados possíveis de uma amizade:
 * - PENDENTE → Solicitação enviada.
 * - ACEITO → Usuários agora são amigos.
 * - RECUSADO → Solicitação foi negada.
 *
 * Funcionalidades:
 * - Enviar solicitação
 * - Responder solicitação (aceitar/recusar)
 * - Remover amizade
 * - Listar solicitações pendentes
 * - Listar amigos
 *
 * Toda lógica de validação é delegada ao AmizadeService.
 * Os retornos utilizam DTOs para expor somente informações essenciais ao frontend.
 */
/**
 * Controller REST responsável pela gestão de Amizades.
 * 
 * Camada: Presentation / REST Controller (Social Context)
 * Contexto de Negócio: Solicitações, aceitação, recusa e remoção de conexões
 * entre usuários.
 * Padrões aplicados: RestController, CrossOrigin, DTOs.
 * 
 * @see AmizadeService
 * @see docs/domain/social_context.md
 * @see REQ-020 (Gestão de Amizades)
 */
@RestController
@RequestMapping("/api/amizades")
@CrossOrigin(origins = "http://localhost:4200")
public class AmizadeController {

    private final AmizadeService amizadeService;

    public AmizadeController(AmizadeService amizadeService) {
        this.amizadeService = amizadeService;
    }

    // =====================================================================
    // 1. ENVIAR SOLICITAÇÃO DE AMIZADE
    // =====================================================================
    /**
     * Envia uma solicitação de amizade para outro usuário.
     *
     * Regras aplicadas no service:
     * - Usuários devem existir.
     * - Não pode haver solicitação duplicada.
     * - Não pode enviar amizade para si mesmo.
     *
     * @param dto contém IDs do solicitante e destinatário.
     * @return Solicitação criada com status PENDENTE.
     */
    @PostMapping
    public ResponseEntity<AmizadeResponseDTO> enviarSolicitacao(
            @Valid @RequestBody AmizadeDTO dto) {
        System.out.println("[POST /api/amizades] Solicitante=" + dto.getSolicitanteId() + ", Destinatário="
                + dto.getDestinatarioId());
        Amizade amizade = amizadeService.enviarSolicitacao(dto);
        AmizadeResponseDTO response = AmizadeMapper.toResponse(amizade);
        System.out.println("[POST /api/amizades] Sucesso: ID=" + amizade.getId() + ", Status=" + amizade.getStatus());
        return ResponseEntity
                .created(URI.create("/api/amizades/" + amizade.getId()))
                .body(response);
    }

    // =====================================================================
    // 2. RESPONDER SOLICITAÇÃO
    // =====================================================================
    /**
     * Permite aceitar ou recusar uma solicitação de amizade.
     *
     * Regras:
     * - Apenas solicitações PENDENTES podem ser respondidas.
     * - "aceitar" → status passa a ACEITO.
     * - "recusar" → status passa a RECUSADO.
     *
     * @param id   ID da solicitação
     * @param acao aceita "aceitar" ou "recusar"
     * @return Solicitação atualizada.
     */
    @PatchMapping("/{id}/resposta")
    public ResponseEntity<AmizadeResponseDTO> responderSolicitacao(
            @PathVariable UUID id,
            @RequestParam String acao) {
        System.out.println("[PATCH /api/amizades/" + id + "/resposta] Ação=" + acao);
        Amizade amizade = amizadeService.responderSolicitacao(id, acao);
        System.out.println("[PATCH /api/amizades/" + id + "/resposta] Novo status=" + amizade.getStatus());
        return ResponseEntity.ok(AmizadeMapper.toResponse(amizade));
    }

    // =====================================================================
    // 3. REMOVER AMIZADE
    // =====================================================================
    /**
     * Remove qualquer relação de amizade, independentemente do estado.
     *
     * @param id ID da relação de amizade
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAmizade(@PathVariable UUID id) {
        amizadeService.removerAmizade(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================================
    // 4. LISTAR SOLICITAÇÕES PENDENTES
    // =====================================================================
    /**
     * Lista todas as solicitações PENDENTES relacionadas ao usuário
     * (tanto enviadas quanto recebidas).
     *
     * @param usuarioId ID do usuário
     * @return Lista de solicitações pendentes
     */
    @GetMapping("/pendentes/{usuarioId}")
    public ResponseEntity<List<AmizadeResponseDTO>> listarPendentes(
            @PathVariable UUID usuarioId) {
        System.out.println("[GET /api/amizades/pendentes/" + usuarioId + "] Buscando pendentes");
        List<Amizade> lista = amizadeService.listarSolicitacoesPendentes(usuarioId);
        System.out.println("[GET /api/amizades/pendentes/" + usuarioId + "] Total: " + lista.size());
        return ResponseEntity.ok(
                lista.stream().map(AmizadeMapper::toResponse).toList());
    }

    // =====================================================================
    // 5. LISTAR AMIGOS
    // =====================================================================
    /**
     * Retorna todos os vínculos de amizade já aceitos do usuário.
     *
     * @param usuarioId ID do usuário
     * @return Lista de amigos
     */
    @GetMapping("/amigos/{usuarioId}")
    public ResponseEntity<List<AmizadeResponseDTO>> listarAmigos(
            @PathVariable UUID usuarioId) {
        System.out.println("[GET /api/amizades/amigos/" + usuarioId + "] Listando amigos");
        List<Amizade> lista = amizadeService.listarAmigos(usuarioId);
        System.out.println("[GET /api/amizades/amigos/" + usuarioId + "] Total: " + lista.size());
        return ResponseEntity.ok(
                lista.stream().map(AmizadeMapper::toResponse).toList());
    }
}