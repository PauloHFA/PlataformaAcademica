package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;
import com.plataforma_academica.plataforma.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * ============================================================================
 *  POSTAGENS CONTROLLER — Plataforma Acadêmica
 * ============================================================================
 *
 * Esta classe é responsável por gerenciar todas as operações relacionadas às
 * postagens feitas pelos usuários dentro da plataforma acadêmica.
 *
 * Cada postagem contém:
 *  - título
 *  - conteúdo
 *  - autor (usuário)
 *  - plataforma associada
 *
 * Funcionalidades expostas por este controller:
 *  ---------------------------------------------------------------------------
 *   ✔ Listar todas as postagens
 *   ✔ Buscar postagem por ID
 *   ✔ Buscar postagem por título
 *   ✔ Criar nova postagem (publicar)
 *   ✔ Atualizar postagem existente
 *   ✔ Deletar postagem
 *  ---------------------------------------------------------------------------
 *
 * O uso de DTOs garante que apenas os dados necessários sejam enviados ao front-end.
 *
 * Este controller se comunica com o PostagemService, que contém toda a lógica
 * de negócio, como validações, regras de atualização e persistência no banco.
 * ============================================================================
 */
@RestController
@RequestMapping("/api/postagens")
@CrossOrigin(origins = "http://localhost:4200") // Permitir uso pelo Angular
public class PostagemController {

    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    // =========================================================================
    // LISTAR TODAS AS POSTAGENS
    // =========================================================================
    /**
     * Retorna todas as postagens cadastradas no sistema.
     *
     * Pode ser utilizado em:
     * - feed geral da plataforma
     * - listagem administrativa
     * - histórico de atividades
     *
     * @return lista de PostagemDTO
     */
    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> listar() {
        return ResponseEntity.ok(postagemService.listarTodasResponse());
    }

    // =========================================================================
    // BUSCAR POSTAGEM POR ID
    // =========================================================================
    /**
     * Busca uma postagem específica pelo seu ID.
     *
     * Regras:
     * - Caso a postagem não exista, retorna 404 Not Found.
     *
     * @param id ID da postagem
     * @return PostagemDTO encontrada ou erro 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> buscarPorId(@PathVariable Long id) {
        PostagemResponseDTO p = postagemService.buscarPorIdResponse(id);

        if (p == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(p);
    }

    // =========================================================================
    // BUSCAR POSTAGENS POR TÍTULO
    // =========================================================================
    /**
     * Busca todas as postagens cujo título contenha o texto informado.
     *
     * Útil para:
     * - barra de pesquisa
     * - filtros de listagens
     *
     * Caso nenhum resultado seja encontrado, retorna 204 No Content.
     *
     * @param titulo Texto a ser pesquisado no título
     * @return lista de PostagemDTO contendo resultados
     */
    @GetMapping("/titulo")
    public ResponseEntity<List<PostagemResponseDTO>> buscarPorTitulo(@RequestParam String titulo) {
        List<PostagemResponseDTO> resultados = postagemService.buscarPorTituloResponse(titulo);

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(resultados);
    }

    // =========================================================================
    // PUBLICAR NOVA POSTAGEM
    // =========================================================================
    /**
     * Cria e publica uma nova postagem na plataforma.
     *
     * O DTO deve conter:
     * - título
     * - conteúdo
     * - ID do autor
     * - ID da plataforma
     *
     * Validações importantes tratadas no service:
     * - existência do autor
     * - existência da plataforma vinculada
     * - formatação dos dados
     *
     * @param request dados da postagem
     * @return postagem criada + URI de localização
     */
    @PostMapping
    public ResponseEntity<PostagemResponseDTO> publicar(@Valid @RequestBody PostagemDTO request) {
        PostagemResponseDTO salvo = postagemService.publicarResponse(request);

        return ResponseEntity
                .created(URI.create("/api/postagens/" + salvo.getId()))
                .body(salvo);
    }

    // =========================================================================
    // ATUALIZAR POSTAGEM EXISTENTE
    // =========================================================================
    /**
     * Atualiza uma postagem existente.
     *
     * Regras:
     * - O ID deve existir e ser informado na URL.
     * - O request também deve conter o ID para consistência.
     * - A lógica de atualização é gerenciada pelo service.
     *
     * @param id ID da postagem
     * @param request dados atualizados
     * @return postagem atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PostagemDTO request
    ) {
        request.setId(id); // garantir consistência
        PostagemResponseDTO atualizado = postagemService.atualizarResponse(request);

        return ResponseEntity.ok(atualizado);
    }

    // =========================================================================
    // DELETAR POSTAGEM
    // =========================================================================
    /**
     * Remove uma postagem pelo seu ID.
     *
     * Caso o ID não exista, o service trata silenciosamente ou lança exception
     * de acordo com a regra de negócio definida.
     *
     * @param id ID da postagem
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        postagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
