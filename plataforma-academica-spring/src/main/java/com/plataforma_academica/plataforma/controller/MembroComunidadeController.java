package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.service.MembroComunidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * ==========================================================================================================
 *  MEMBRO COMUNIDADE CONTROLLER — Plataforma Acadêmica
 * ==========================================================================================================
 * Controlador responsável pelo gerenciamento dos vínculos entre usuários e
 * comunidades. Cada instância de MembroComunidade representa um usuário que
 * participa de uma comunidade específica.
 *
 * Funções principais:
 *   • Listar membros
 *   • Buscar membro por ID
 *   • Listar membros de uma comunidade
 *   • Listar comunidades de um usuário
 *   • Verificar se um usuário pertence a uma comunidade
 *   • Criar vínculo (usuário entra na comunidade)
 *   • Remover vínculo (usuário sai da comunidade)
 *
 * Regras:
 *   • A controller trata apenas validações simples (ex.: retornos HTTP).
 *   • Toda lógica de negócio (impedir duplicidade, validar dono, garantir existência)
 *     é realizada pelo MembroComunidadeService.
 *
 * Endpoints expostos:
 *   GET     /api/membrocomunidade                    → Listar todos
 *   GET     /api/membrocomunidade/{id}               → Buscar por ID
 *   GET     /api/membrocomunidade/usuario/{id}       → Buscar vínculos de um usuário
 *   GET     /api/membrocomunidade/comunidade/{id}    → Buscar membros de uma comunidade
 *   GET     /api/membrocomunidade/existe             → Verificar vínculo usuário+comunidade
 *   POST    /api/membrocomunidade                    → Criar vínculo
 *   DELETE  /api/membrocomunidade/{id}               → Remover vínculo
 *
 * ==========================================================================================================
 */
/**
 * Controller REST responsável pelo gerenciamento de Membros de Comunidades.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Social / Associação de usuários a comunidades.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see MembroComunidadeService
 * @see docs/domain/social_context.md
 * @see REQ-016 (Gestão de Membros em Comunidades)
 */
@RestController
@RequestMapping("/api/membrocomunidade")
@CrossOrigin(origins = "http://localhost:4200")
public class MembroComunidadeController {

    @Autowired
    private MembroComunidadeService membroComunidadeService;

    // ======================================================================================================
    // 1. LISTAR TODOS
    // ======================================================================================================
    /**
     * Retorna todos os vínculos registrados no sistema.
     *
     * Principal uso:
     * • Administração
     * • Auditoria
     * • Depuração
     *
     * @return lista de todos os membros-comunidades.
     */
    @GetMapping
    public ResponseEntity<List<MembroComunidade>> listarTodos() {
        return ResponseEntity.ok(membroComunidadeService.listarTodos());
    }

    // ======================================================================================================
    // 2. BUSCAR POR ID
    // ======================================================================================================
    /**
     * Obtém um vínculo específico pelo ID.
     *
     * @param id ID do vínculo.
     * @return 200 OK se encontrado, 404 Not Found caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MembroComunidade> buscarPorId(@PathVariable UUID id) {
        MembroComunidade membro = membroComunidadeService.buscarPorId(id);

        if (membro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membro);
    }

    // ======================================================================================================
    // 3. BUSCAR POR USUÁRIO
    // ======================================================================================================
    /**
     * Lista todas as comunidades que um usuário participa.
     *
     * @param usuarioId ID do usuário.
     * @return lista de vínculos associados ao usuário.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MembroComunidade>> buscarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(membroComunidadeService.buscarPorUsuario(usuarioId));
    }

    // ======================================================================================================
    // 4. BUSCAR POR COMUNIDADE
    // ======================================================================================================
    /**
     * Lista todos os usuários que participam de uma comunidade.
     *
     * Usado em:
     * • Tela da comunidade
     * • Administração
     *
     * @param comunidadeId ID da comunidade.
     * @return lista de membros.
     */
    @GetMapping("/comunidade/{comunidadeId}")
    public ResponseEntity<List<MembroComunidade>> buscarPorComunidade(@PathVariable UUID comunidadeId) {
        return ResponseEntity.ok(membroComunidadeService.buscarPorComunidade(comunidadeId));
    }

    // ======================================================================================================
    // 5. VERIFICAR SE USUÁRIO PERTENCE À COMUNIDADE
    // ======================================================================================================
    /**
     * Verifica se um usuário já participa de uma comunidade.
     *
     * @param usuarioId    ID do usuário.
     * @param comunidadeId ID da comunidade.
     * @return vínculo existente ou 404 Not Found.
     */
    @GetMapping("/existe")
    public ResponseEntity<MembroComunidade> buscarPorUsuarioEComunidade(
            @RequestParam UUID usuarioId,
            @RequestParam UUID comunidadeId) {
        MembroComunidade membro = membroComunidadeService.buscarPorUsuarioEComunidade(usuarioId, comunidadeId);

        if (membro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membro);
    }

    // ======================================================================================================
    // 6. ADICIONAR MEMBRO (ENTRAR)
    // ======================================================================================================
    /**
     * Cria um vínculo entre usuário e comunidade.
     *
     * Validações feitas aqui:
     * • Verifica duplicidade antes de salvar (usuário não entra duas vezes).
     *
     * Validações feitas no service:
     * • Verificar se a comunidade existe.
     * • Verificar se o usuário existe.
     * • Regra de limite de membros / papéis.
     *
     * @param membro Objeto contendo usuário e comunidade.
     * @return vínculo criado ou 409 Conflict caso já exista.
     */
    @PostMapping
    public ResponseEntity<MembroComunidade> adicionar(@RequestBody MembroComunidade membro) {

        MembroComunidade existente = membroComunidadeService.buscarPorUsuarioEComunidade(
                membro.getUsuario().getId(),
                membro.getComunidade().getId());

        if (existente != null) {
            return ResponseEntity.status(409).body(existente);
        }

        MembroComunidade salvo = membroComunidadeService.salvar(membro);
        URI location = URI.create("/api/membrocomunidade/" + salvo.getId());

        return ResponseEntity.created(location).body(salvo);
    }

    // ======================================================================================================
    // 7. REMOVER MEMBRO (SAIR)
    // ======================================================================================================
    /**
     * Remove o vínculo entre usuário e comunidade pelo ID do membro.
     *
     * @param id ID do vínculo.
     * @return 204 No Content caso exista, 404 se não encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {

        MembroComunidade existente = membroComunidadeService.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        membroComunidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}