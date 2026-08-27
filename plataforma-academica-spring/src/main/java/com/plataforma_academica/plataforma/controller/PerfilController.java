package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ==========================================================================================================
 *  PERFIL CONTROLLER — Plataforma Acadêmica
 * ==========================================================================================================
 * Controlador responsável por gerenciar os perfis de usuários do sistema.
 *
 * Cada usuário pode ter **exatamente um perfil**, que pode conter informações como:
 *   • biografia (bio)
 *   • foto de perfil
 *   • curso
 *
 * Este controller é responsável apenas pela comunicação HTTP,
 * delegando regras de negócio ao PerfilService.
 *
 * Funcionalidades expostas:
 *   GET     /api/perfis                               → Listar todos os perfis
 *   POST    /api/perfis                               → Criar perfil
 *   PUT     /api/perfis/{id}                          → Atualizar perfil
 *   GET     /api/perfis/{id}                          → Buscar por ID
 *   GET     /api/perfis/curso/{curso}                 → Buscar perfis por curso
 *   GET     /api/perfis/usuario/{usuarioId}           → Buscar perfil pelo ID do usuário
 *   GET     /api/perfis/existe/{usuarioId}            → Verificar se o usuário tem perfil
 *
 * Regras principais (implementadas no service):
 *   • O usuário deve existir para criar um perfil.
 *   • Um usuário pode ter somente **um** perfil.
 *   • Atualizações só são permitidas para perfis existentes.
 *
 * ==========================================================================================================
 */
/**
 * Controller REST responsável pelo gerenciamento de Perfis acadêmicos.
 * 
 * Camada: Presentation / REST Controller (Identity Context)
 * Contexto de Negócio: Bio, foto, curso e dados complementares de usuário.
 * Padrões aplicados: RestController, CrossOrigin, DTOs.
 * 
 * @see PerfilService
 * @see docs/domain/identity_context.md
 * @see REQ-002 (Perfil Acadêmico)
 */
@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = "http://localhost:4200")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    // ======================================================================================================
    // 1. LISTAR TODOS OS PERFIS
    // ======================================================================================================
    /**
     * Retorna a lista de todos os perfis cadastrados no sistema.
     *
     * Usado em:
     * • Painéis administrativos
     * • Páginas de listagem geral de usuários
     *
     * @return lista completa de perfis.
     */
    @GetMapping
    public ResponseEntity<List<Perfil>> listar() {
        return ResponseEntity.ok(perfilService.listarTodos());
    }

    // ======================================================================================================
    // 2. CRIAR PERFIL
    // ======================================================================================================
    /**
     * Cria um novo perfil para um usuário.
     *
     * Regras:
     * • O usuário deve existir.
     * • O usuário só pode ter um perfil.
     * • O DTO contém apenas as informações necessárias (bio, curso, foto).
     *
     * @param dto Dados do perfil que será criado.
     * @return o perfil criado.
     */
    @PostMapping
    public ResponseEntity<Perfil> criar(@RequestBody PerfilDTO dto) {
        Perfil perfil = perfilService.salvar(dto);
        return ResponseEntity.ok(perfil);
    }

    // ======================================================================================================
    // 3. BUSCAR PERFIL POR ID
    // ======================================================================================================
    /**
     * Retorna um perfil específico pelo ID.
     *
     * @param id ID do perfil.
     * @return 200 OK se encontrado, ou 404 Not Found se não existir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Perfil> buscarPorId(@PathVariable Long id) {
        Perfil perfil = perfilService.buscarPorId(id);

        if (perfil == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(perfil);
    }

    // ======================================================================================================
    // 4. ATUALIZAR PERFIL
    // ======================================================================================================
    /**
     * Atualiza os dados de um perfil existente.
     *
     * O DTO permite alterar:
     * • bio
     * • foto de perfil
     * • curso
     *
     * @param id  ID do perfil.
     * @param dto dados atualizados.
     * @return perfil atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> atualizar(@PathVariable Long id, @RequestBody PerfilDTO dto) {
        Perfil perfil = perfilService.atualizar(id, dto);
        return ResponseEntity.ok(perfil);
    }

    // ======================================================================================================
    // 5. BUSCAR PERFIS POR CURSO
    // ======================================================================================================
    /**
     * Lista todos os perfis associados a um curso específico.
     *
     * Útil para:
     * • Grupos de curso
     * • Integração entre alunos
     * • Páginas personalizadas por área de estudo
     *
     * @param curso nome do curso.
     * @return lista de perfis encontrados.
     */
    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<Perfil>> buscarPorCurso(@PathVariable String curso) {
        return ResponseEntity.ok(perfilService.buscarPorCurso(curso));
    }

    // ======================================================================================================
    // 6. BUSCAR PERFIL PELO ID DO USUÁRIO
    // ======================================================================================================
    /**
     * Obtém o perfil associado a um usuário.
     *
     * Cada usuário possui no máximo um perfil.
     *
     * @param usuarioId ID do usuário.
     * @return o perfil do usuário ou 404 caso não exista.
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Perfil> buscarPorUsuario(@PathVariable Long usuarioId) {
        Perfil perfil = perfilService.buscarPorUsuarioId(usuarioId);

        if (perfil == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(perfil);
    }

    // ======================================================================================================
    // 7. VERIFICAR SE USUÁRIO POSSUI PERFIL
    // ======================================================================================================
    /**
     * Verifica se um usuário já possui perfil.
     *
     * Usado para:
     * • Bloquear criação duplicada de perfil
     * • Redirecionar o usuário para criar ou editar perfil
     *
     * @param usuarioId ID do usuário.
     * @return true se existir perfil, false caso contrário.
     */
    @GetMapping("/existe/{usuarioId}")
    public ResponseEntity<Boolean> existePerfil(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(perfilService.existePerfilDoUsuario(usuarioId));
    }
}