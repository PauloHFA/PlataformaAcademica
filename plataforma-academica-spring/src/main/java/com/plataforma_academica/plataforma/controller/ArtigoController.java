package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.dto.ArtigoResponseDTO;
import com.plataforma_academica.plataforma.mapper.ArtigoMapper;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.service.ArtigoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controller REST responsável pelo gerenciamento de Artigos acadêmicos.
 * 
 * Camada: Presentation / REST Controller (Academic Context)
 * Contexto de Negócio: Publicação de artigos, pesquisas e ensaios acadêmicos.
 * Padrões aplicados: RestController, CrossOrigin, DTOs, Validação.
 * 
 * @see ArtigoService
 * @see docs/domain/academic_context.md
 * @see REQ-010 (Publicação de Artigos Acadêmicos)
 */
@RestController
 *
 * Esse componente expõe endpoints REST que permitem:
 *   • Criar novos artigos
 *   • Editar artigos existentes (restrito ao autor)
 *   • Excluir artigos (restrito ao autor)
 *   • Buscar artigo por ID
 *   • Listar todos os artigos
 *   • Listar artigos por autor
 *
 * O controller NÃO contém regras de negócio. Ele apenas:
 *   ✔ Recebe as requisições do frontend
 *   ✔ Encaminha os dados ao ArtigoService
 *   ✔ Retorna respostas padronizadas ao cliente
 *
 * Regras importantes:
 *   • Toda validação de autor, integridade e permissão é realizada exclusivamente no ArtigoService.
 *   • A comunicação com o frontend é feita por meio de DTOs (ArtigoDTO e ArtigoResponseDTO),
 *     garantindo segurança e isolamento da entidade real.
 *
 * Endpoints disponíveis:
 *   POST   /api/artigos                → Criar artigo
 *   PUT    /api/artigos/{id}           → Editar artigo
 *   DELETE /api/artigos/{id}           → Remover artigo
 *   GET    /api/artigos/{id}           → Buscar artigo por ID
 *   GET    /api/artigos                → Listar todos os artigos
 *   GET    /api/artigos/autor/{autorId}→ Listar artigos de um autor
 *
 * @see ArtigoService
 * @see docs/domain/academic_context.md
 * @see REQ-010 (Publicação de Artigos Acadêmicos)
 */
@RestController
@RequestMapping("/api/artigos")
@CrossOrigin(origins = "http://localhost:4200")
public class ArtigoController {

    private final ArtigoService artigoService;

    public ArtigoController(ArtigoService artigoService) {
        this.artigoService = artigoService;
    }

    // =====================================================================
    // 1. CRIAR ARTIGO
    // =====================================================================
    /**
     * Cria um novo artigo na plataforma.
     *
     * Fluxo:
     * - Recebe um ArtigoDTO contendo título, conteúdo e autorId.
     * - O service valida se o autor existe e se os dados são válidos.
     * - O artigo é salvo no banco de dados.
     * - A URI do novo recurso é retornada no cabeçalho Location.
     *
     * @param dto Dados enviados pelo cliente contendo título, conteúdo e ID do autor.
     * @return ResponseEntity 201 Created com o ArtigoResponseDTO criado.
     */
    @PostMapping
    public ResponseEntity<ArtigoResponseDTO> criar(@Valid @RequestBody ArtigoDTO dto) {
        Artigo artigo = artigoService.criar(dto);
        ArtigoResponseDTO response = ArtigoMapper.toResponse(artigo);

        return ResponseEntity
                .created(URI.create("/api/artigos/" + artigo.getId()))
                .body(response);
    }

    // =====================================================================
    // 2. EDITAR ARTIGO
    // =====================================================================
    /**
     * Edita um artigo existente.
     *
     * Regras aplicadas:
     * - Apenas o autor pode editar o próprio artigo.
     * - O service valida a existência do artigo e atualiza os campos.
     *
     * @param id  ID do artigo que será editado.
     * @param dto Dados novos enviados pelo cliente.
     * @return ResponseEntity contendo o artigo já atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArtigoResponseDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody ArtigoDTO dto
    ) {
        Artigo artigo = artigoService.editar(id, dto);
        return ResponseEntity.ok(ArtigoMapper.toResponse(artigo));
    }

    // =====================================================================
    // 3. DELETAR ARTIGO
    // =====================================================================
    /**
     * Exclui um artigo permanentemente.
     *
     * Regras:
     * - Apenas o autor tem permissão para deletar o artigo.
     * - O service valida o autor e executa a remoção.
     *
     * @param id ID do artigo a ser removido.
     * @param solicitanteId ID do usuário que está solicitando a exclusão.
     * @return ResponseEntity 204 No Content se a operação for concluída.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam Long solicitanteId
    ) {
        artigoService.deletar(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }

    // =====================================================================
    // 4. BUSCAR ARTIGO POR ID
    // =====================================================================
    /**
     * Busca um artigo específico pelo seu ID.
     *
     * @param id ID do artigo buscado.
     * @return ResponseEntity contendo ArtigoResponseDTO do artigo encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArtigoResponseDTO> buscar(@PathVariable Long id) {
        Artigo artigo = artigoService.buscarPorId(id);
        return ResponseEntity.ok(ArtigoMapper.toResponse(artigo));
    }

    // =====================================================================
    // 5. LISTAR TODOS OS ARTIGOS
    // =====================================================================
    /**
     * Lista todos os artigos cadastrados.
     *
     * @return ResponseEntity com lista de ArtigoResponseDTO.
     */
    @GetMapping
    public ResponseEntity<List<ArtigoResponseDTO>> listar() {
        List<Artigo> lista = artigoService.listarTodos();
        return ResponseEntity.ok(lista.stream().map(ArtigoMapper::toResponse).toList());
    }

    // =====================================================================
    // 6. LISTAR ARTIGOS POR AUTOR
    // =====================================================================
    /**
     * Lista todos os artigos criados por um determinado autor.
     *
     * @param autorId ID do autor.
     * @return Lista de artigos pertencentes ao autor.
     */
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<ArtigoResponseDTO>> porAutor(@PathVariable Long autorId) {
        List<Artigo> lista = artigoService.listarPorAutor(autorId);
        return ResponseEntity.ok(lista.stream().map(ArtigoMapper::toResponse).toList());
    }
}