package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.dto.AtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.AtividadeMapper;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.SaladeAulaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================================
 *  SALA DE AULA CONTROLLER — Plataforma Acadêmica
 * ============================================================================
 *
 * Este controlador é responsável por toda a orquestração da funcionalidade de
 * "Sala de Aula" dentro da plataforma acadêmica. Ele expõe endpoints REST que
 * permitem:
 *
 * ---------------------------------------------------------------------------
 *  ✔ Criar salas de aula
 *  ✔ Listar salas
 *  ✔ Buscar sala por ID
 *  ✔ Excluir salas
 *
 *  ✔ Adicionar membros à sala
 *  ✔ Remover membros
 *  ✔ Listar membros
 *
 *  ✔ Criar atividades dentro da sala
 *  ✔ Atualizar atividades
 *  ✔ Remover atividades
 *  ✔ Listar atividades da sala
 *  ✔ Buscar atividade por ID
 * ---------------------------------------------------------------------------
 *
 * REGRAS DE NEGÓCIO IMPORTANTES:
 * - Somente o CRIADOR da sala pode:
 *      • adicionar membros
 *      • remover membros
 *      • criar atividades
 *      • atualizar atividades
 *      • deletar atividades
 *      • deletar a sala
 *
 * - Membros comuns podem:
 *      • visualizar sala
 *      • visualizar membros
 *      • visualizar atividades
 *
 * O controller permanece fino: toda a lógica de validação, regras de permissão,
 * e processamento dos dados é delegada ao SaladeAulaService.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/saladeaula")
@CrossOrigin(origins = "http://localhost:4200")
public class SaladeAulaController {

    private final SaladeAulaService salaService;

    public SaladeAulaController(SaladeAulaService salaService) {
        this.salaService = salaService;
    }

    // =========================================================================
    //  CRUD DA SALA DE AULA
    // =========================================================================

    /**
     * Cria uma nova sala de aula.
     *
     * O usuário criador deve ser enviado via path variable.
     * Apenas usuários válidos podem criar salas.
     *
     * @param sala objeto com nome, descrição, etc.
     * @param criadorId ID do usuário criador
     */
    @PostMapping("/criar/{criadorId}")
    public ResponseEntity<SaladeAula> criarSala(@RequestBody SaladeAula sala,
                                                @PathVariable Long criadorId) {
        return ResponseEntity.ok(salaService.criarSala(sala, criadorId));
    }

    /**
     * Lista todas as salas existentes.
     */
    @GetMapping
    public ResponseEntity<List<SaladeAula>> listarSalas() {
        return ResponseEntity.ok(salaService.listarTodasSalas());
    }

    /**
     * Busca uma sala pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaladeAula> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.buscarSalaPorId(id));
    }

    /**
     * Deleta uma sala.
     *
     * Somente o criador pode realizar essa ação.
     *
     * @param id ID da sala
     * @param userId ID do possível criador
     */
    @DeleteMapping("/{id}/usuario/{userId}")
    public ResponseEntity<Void> deletarSala(@PathVariable Long id,
                                            @PathVariable Long userId) {
        salaService.deletarSala(id, userId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    //  GERENCIAMENTO DE MEMBROS
    // =========================================================================

    /**
     * Adiciona um membro à sala.
     *
     * Somente o criador da sala pode adicionar novos membros.
     *
     * @param salaId ID da sala
     * @param membroId ID do usuário a ser adicionado
     * @param creatorId ID do criador da sala
     */
    @PostMapping("/{salaId}/add-membro/{membroId}/criador/{creatorId}")
    public ResponseEntity<Usuario> adicionarMembro(
            @PathVariable Long salaId,
            @PathVariable Long membroId,
            @PathVariable Long creatorId
    ) {
        return ResponseEntity.ok(salaService.adicionarMembro(salaId, membroId, creatorId));
    }

    /**
     * Lista todos os membros de uma sala.
     */
    @GetMapping("/{salaId}/membros")
    public ResponseEntity<List<Usuario>> listarMembros(@PathVariable Long salaId) {
        return ResponseEntity.ok(salaService.listarMembros(salaId));
    }

    /**
     * Remove um membro.
     *
     * Regras:
     * - O criador deve autorizar
     * - O membro deve estar na sala
     */
    @DeleteMapping("/{salaId}/remover-membro/{membroId}/criador/{creatorId}")
    public ResponseEntity<Void> removerMembro(
            @PathVariable Long salaId,
            @PathVariable Long membroId,
            @PathVariable Long creatorId
    ) {
        salaService.removerMembro(salaId, membroId, creatorId);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    //  GERENCIAMENTO DE ATIVIDADES
    // =========================================================================

    /**
     * Cria uma nova atividade na sala.
     *
     * Apenas o criador pode adicionar atividades.
     *
     * @param salaId ID da sala
     * @param creatorId ID do criador
     * @param atividadeDTO objeto com título, descrição, etc.
     */
    @PostMapping("/{salaId}/atividade/criar/{creatorId}")
    public ResponseEntity<AtividadeResponseDTO> criarAtividade(
            @PathVariable Long salaId,
            @PathVariable Long creatorId,
            @RequestBody AtividadeDTO atividadeDTO
    ) {
        Atividade atividade = salaService.cadastrarAtividade(salaId, atividadeDTO, creatorId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
    }

    /**
     * Lista todas as atividades da sala.
     */
    @GetMapping("/{salaId}/atividades")
    public ResponseEntity<List<AtividadeResponseDTO>> listarAtividades(@PathVariable Long salaId) {
        List<Atividade> atividades = salaService.listarAtividadesPorSala(salaId);
        return ResponseEntity.ok(atividades.stream().map(AtividadeMapper::toResponse).toList());
    }

    /**
     * Busca atividade por ID.
     */
    @GetMapping("/atividade/{atividadeId}")
    public ResponseEntity<AtividadeResponseDTO> buscarAtividade(@PathVariable Long atividadeId) {
        Atividade atividade = salaService.buscarAtividadePorId(atividadeId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
    }

    /**
     * Atualiza uma atividade.
     *
     * Somente o criador da sala pode realizar esta ação.
     */
    @PutMapping("/{salaId}/atividade/atualizar/{creatorId}")
    public ResponseEntity<AtividadeResponseDTO> atualizarAtividade(
            @PathVariable Long salaId,
            @PathVariable Long creatorId,
            @RequestBody AtividadeDTO atividadeDTO
    ) {
        Atividade atividade = salaService.atualizarAtividade(salaId, atividadeDTO, creatorId);
        return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
    }

    /**
     * Deleta uma atividade.
     *
     * Apenas o criador da sala pode remover atividades.
     */
    @DeleteMapping("/atividade/{atividadeId}/criador/{creatorId}")
    public ResponseEntity<Void> deletarAtividade(
            @PathVariable Long atividadeId,
            @PathVariable Long creatorId
    ) {
        salaService.deletarAtividade(atividadeId, creatorId);
        return ResponseEntity.noContent().build();
    }
}