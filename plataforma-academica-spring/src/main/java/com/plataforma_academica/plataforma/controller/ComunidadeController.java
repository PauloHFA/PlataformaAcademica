package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.service.ComunidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST responsável pelo gerenciamento de Comunidades.
 * 
 * Camada: Presentation / REST Controller (Social Context)
 * Contexto de Negócio: Grupos temáticos com membros e interações sociais.
 * Padrões aplicados: RestController, CrossOrigin, Validação.
 * 
 * @see ComunidadeService
 * @see docs/domain/social_context.md
 * @see REQ-015 (Criação de Comunidades)
 */
@RestController Uma comunidade funciona como um grupo temático
 * onde usuários podem interagir, discutir, compartilhar conteúdo e colaborar.
 *
 * Objetivos principais desta controller:
 *   • Expor endpoints REST de forma clara e padronizada.
 *   • Realizar validações básicas (@Valid, @PathVariable, @RequestParam).
 *   • Encaminhar toda a lógica de negócio para o ComunidadeService.
 *   • Garantir respostas HTTP adequadas para cada operação.
 *
 * Regras e validações importantes:
 *   • Verificação de dono da comunidade.
 *   • Controle de entrada e saída dos membros.
 *   • Impedir duplicidade de membros.
 *   • Garantir que IDs existam antes de operar.
 *   (Todas processadas exclusivamente no service).
 *
 * Endpoints disponíveis:
 *   POST    /api/comunidades            → Criar comunidade
 *   DELETE  /api/comunidades/{id}       → Deletar comunidade (somente dono)
 *   POST    /api/comunidades/{id}/entrar → Entrar na comunidade
 *   POST    /api/comunidades/{id}/sair   → Sair da comunidade
 *   GET     /api/comunidades            → Listar todas as comunidades
 *   GET     /api/comunidades/{id}/membros → Listar membros de uma comunidade
 *
 * @see ComunidadeService
 * @see docs/domain/social_context.md
 * @see REQ-015 (Criação de Comunidades)
 */
@RestController
@RequestMapping("/api/comunidades")
@CrossOrigin(origins = "http://localhost:4200")
public class ComunidadeController {

    private final ComunidadeService comunidadeService;

    public ComunidadeController(ComunidadeService comunidadeService) {
        this.comunidadeService = comunidadeService;
    }

    // ======================================================================================================
    // 1. CRIAR COMUNIDADE
    // ======================================================================================================
    /**
     * Cria uma nova comunidade na plataforma.
     *
     * Regras:
     *   • O usuário informado no DTO torna-se automaticamente o dono.
     *   • O dono já entra como membro com papel ADMIN.
     *   • Validações específicas são tratadas no service.
     *
     * Retorno:
     *   • HTTP 201 Created com o Location do recurso recém-criado.
     *
     * @param dto Dados da comunidade (nome, descrição, idDono).
     * @return Comunidade criada.
     */
    @PostMapping
    public ResponseEntity<Comunidade> criar(@Valid @RequestBody ComunidadeDTO dto) {

        Comunidade comunidadeCriada = comunidadeService.criarComunidade(dto);

        return ResponseEntity
                .created(URI.create("/api/comunidades/" + comunidadeCriada.getId()))
                .body(comunidadeCriada);
    }

    // ======================================================================================================
    // 2. DELETAR COMUNIDADE (somente dono)
    // ======================================================================================================
    /**
     * Exclui permanentemente uma comunidade.
     *
     * Regras:
     *   • Apenas o dono pode deletar.
     *   • Caso o solicitante não seja o dono, uma exceção é lançada.
     *   • Caso a comunidade não exista, ResourceNotFoundException é lançada.
     *
     * @param id ID da comunidade a ser deletada.
     * @param solicitanteId ID do usuário solicitando a exclusão.
     * @return HTTP 204 No Content em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam Long solicitanteId
    ) {
        comunidadeService.deletarComunidade(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }

    // ======================================================================================================
    // 3. ENTRAR NA COMUNIDADE
    // ======================================================================================================
    /**
     * Adiciona um usuário como membro de uma comunidade.
     *
     * Regras:
     *   • Um usuário não pode entrar duas vezes.
     *   • Caso comunidade ou usuário não exista, exceções são lançadas.
     *
     * @param id ID da comunidade.
     * @param usuarioId ID do usuário.
     * @return Vínculo criado (MembroComunidade).
     */
    @PostMapping("/{id}/entrar")
    public ResponseEntity<MembroComunidade> entrar(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        return ResponseEntity.ok(comunidadeService.entrarComunidade(id, usuarioId));
    }

    // ======================================================================================================
    // 4. SAIR DA COMUNIDADE
    // ======================================================================================================
    /**
     * Remove um usuário da comunidade.
     *
     * Regras:
     *   • Caso o usuário não seja membro, exceção é lançada.
     *   • O dono pode sair, mas isso não deleta a comunidade.
     *
     * @param id ID da comunidade.
     * @param usuarioId ID do usuário.
     * @return HTTP 204 No Content em caso de sucesso.
     */
    @PostMapping("/{id}/sair")
    public ResponseEntity<Void> sair(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        comunidadeService.sairComunidade(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // ======================================================================================================
    // 5. LISTAR TODAS AS COMUNIDADES
    // ======================================================================================================
    /**
     * Lista todas as comunidades registradas.
     *
     * Usado em:
     *   • Tela inicial de comunidades
     *   • Exploração e pesquisa
     *   • Módulos administrativos
     *
     * @return Lista geral de comunidades.
     */
    @GetMapping
    public ResponseEntity<List<Comunidade>> listar() {
        return ResponseEntity.ok(comunidadeService.listarTodas());
    }

    // ======================================================================================================
    // 6. LISTAR MEMBROS DE UMA COMUNIDADE
    // ======================================================================================================
    /**
     * Retorna todos os membros pertencentes a uma comunidade.
     *
     * Inclui:
     *   • Dono (ADMIN)
     *   • Membros comuns
     *
     * Usado em:
     *   • Painel da comunidade
     *   • Área de administração
     *   • Visualização pública
     *
     * @param id ID da comunidade.
     * @return Lista de membros.
     */
    @GetMapping("/{id}/membros")
    public ResponseEntity<List<MembroComunidade>> listarMembros(@PathVariable Long id) {
        return ResponseEntity.ok(comunidadeService.listarMembros(id));
    }
}