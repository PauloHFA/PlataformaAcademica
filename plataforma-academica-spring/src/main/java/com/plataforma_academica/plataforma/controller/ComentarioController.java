package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * ==========================================================================================================
 *  COMENTARIO CONTROLLER — Plataforma Acadêmica
 * ==========================================================================================================
 * Controller responsável por gerenciar os comentários criados pelos usuários dentro
 * da plataforma, permitindo interação em postagens, atividades, artigos e outros conteúdos.
 *
 * Funções da controller:
 *   • Disponibilizar endpoints REST para CRUD de comentários.
 *   • Garantir comunicação padronizada com o frontend via HTTP.
 *   • Delegar regras, validações e integridade dos dados ao ComentarioService.
 *
 * Funcionalidades disponíveis:
 *   GET    /comentario             → Listar todos os comentários
 *   GET    /comentario/{id}        → Buscar comentário por ID
 *   POST   /comentario             → Criar novo comentário
 *   PUT    /comentario/{id}        → Atualizar comentário existente
 *   DELETE /comentario/{id}        → Remover comentário por ID
 *
 * Regras importantes:
 *   • IDs inexistentes resultam em HTTP 404.
 *   • O controller não contém lógica de negócio — apenas coordena requisição → service → resposta.
 *   • Todas as validações (existência, vínculos, permissões, etc.) são responsabilidade do service.
 *
 * Objetivo:
 *   Garantir que operações com comentários sejam expostas de forma clara, segura e consistente.
 *
 * Autor: Plataforma Acadêmica Unificada
 * ==========================================================================================================
 */
@RestController
@RequestMapping("/comentario")
@CrossOrigin(origins = "http://localhost:4200")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // ======================================================================================================
    // 1. LISTAR TODOS OS COMENTÁRIOS
    // ======================================================================================================
    /**
     * Retorna todos os comentários cadastrados.
     *
     * Usos comuns:
     *   • Painéis administrativos
     *   • Depuração
     *   • Ferramentas que necessitam carregar todos os comentários existentes
     *
     * @return Lista completa de comentários.
     */
    @GetMapping
    public ResponseEntity<List<Comentario>> listarTodos() {
        return ResponseEntity.ok(comentarioService.listarTodos());
    }

    // ======================================================================================================
    // 2. BUSCAR COMENTÁRIO POR ID
    // ======================================================================================================
    /**
     * Localiza e retorna um comentário pelo seu ID.
     *
     * Regras:
     *   • Caso não exista, retorna HTTP 404.
     *
     * @param id ID do comentário
     * @return Comentário encontrado ou erro 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comentario> buscarPorId(@PathVariable Long id) {
        Comentario comentario = comentarioService.buscarPorId(id);
        if (comentario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comentario);
    }

    // ======================================================================================================
    // 3. CRIAR UM NOVO COMENTÁRIO
    // ======================================================================================================
    /**
     * Cria e cadastra um novo comentário.
     *
     * Detalhes:
     *   • Retorna status 201 Created.
     *   • Inclui header "Location" com o caminho do novo recurso.
     *
     * @param comentario Corpo da requisição contendo o comentário a ser criado.
     * @return Comentário criado com HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Comentario> salvar(@RequestBody Comentario comentario) {

        Comentario salvo = comentarioService.salvar(comentario);
        URI location = URI.create("/comentario/" + salvo.getId());

        return ResponseEntity.created(location).body(salvo);
    }

    // ======================================================================================================
    // 4. ATUALIZAR UM COMENTÁRIO
    // ======================================================================================================
    /**
     * Atualiza os dados de um comentário existente.
     *
     * Regras:
     *   • Se o ID não existir, retorna 404.
     *   • Caso contrário, retorna a versão atualizada.
     *
     * @param id ID do comentário
     * @param comentarioAtualizado Dados enviados pelo cliente
     * @return Comentário atualizado ou HTTP 404 se inexistente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comentario> atualizar(
            @PathVariable Long id,
            @RequestBody Comentario comentarioAtualizado
    ) {
        Comentario atualizado = comentarioService.atualizar(id, comentarioAtualizado);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizado);
    }

    // ======================================================================================================
    // 5. DELETAR UM COMENTÁRIO
    // ======================================================================================================
    /**
     * Remove um comentário de forma permanente.
     *
     * Regras:
     *   • Se não existir, retorna 404.
     *   • Se existir, retorna 204 No Content.
     *
     * @param id ID do comentário
     * @return HTTP 204 ou 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        Comentario existente = comentarioService.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        comentarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}