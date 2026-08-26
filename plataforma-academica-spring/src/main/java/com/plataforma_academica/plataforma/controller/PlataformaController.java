package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.service.PlataformaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ==========================================================================================================
 *  PLATAFORMA CONTROLLER — Plataforma Acadêmica
 * ==========================================================================================================
 * Controlador responsável pelo gerenciamento das Plataformas do sistema.
 *
 * Uma Plataforma representa um módulo, subsistema ou ambiente acessível dentro
 * do ecossistema da aplicação. Exemplos:
 *   • Biblioteca Virtual
 *   • Fórum Acadêmico
 *   • Central de Atividades
 *   • Catálogo de Cursos
 *
 * Este controller implementa a comunicação HTTP e expõe endpoints REST para:
 *   GET    /api/plataforma               → Listar plataformas
 *   POST   /api/plataforma               → Criar plataforma
 *   GET    /api/plataforma/{id}          → Buscar plataforma por ID
 *   PUT    /api/plataforma/{id}          → Atualizar plataforma
 *   DELETE /api/plataforma/{id}          → Remover plataforma
 *
 * Regras e responsabilidades:
 *   • O Controller apenas coordena requisições e retornos HTTP.
 *   • Toda regra de validação, integridade e lógica fica no PlataformaService.
 *   • Retorna corretamente status 200, 404, 204 e demais conforme o contexto.
 *
 * ==========================================================================================================
 */
/**
 * Controller REST responsável pelo escopo de Plataforma.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Aggregate root global da plataforma.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see Plataforma
 */
@RestController
@RequestMapping("/api/plataforma")
@CrossOrigin(origins = "http://localhost:4200")
public class PlataformaController {

    private final PlataformaService plataformaService;

    public PlataformaController(PlataformaService plataformaService) {
        this.plataformaService = plataformaService;
    }

    // ======================================================================================================
    // 1. LISTAR TODAS AS PLATAFORMAS
    // ======================================================================================================
    /**
     * Retorna todas as plataformas cadastradas no sistema.
     *
     * Geralmente utilizado:
     * • Na página inicial
     * • Em dashboards administrativos
     * • Para compor menus e módulos acessíveis ao usuário
     *
     * @return lista com todas as plataformas.
     */
    @GetMapping
    public ResponseEntity<List<Plataforma>> listar() {
        return ResponseEntity.ok(plataformaService.listarTudo());
    }

    // ======================================================================================================
    // 2. CRIAR NOVA PLATAFORMA
    // ======================================================================================================
    /**
     * Cria uma nova plataforma.
     *
     * Regras:
     * • O JSON enviado deve representar uma plataforma válida.
     * • Validações como nome duplicado podem ser aplicadas no service.
     *
     * @param plataforma objeto recebido no corpo da requisição.
     * @return plataforma criada.
     */
    @PostMapping
    public ResponseEntity<Plataforma> criar(@RequestBody Plataforma plataforma) {
        Plataforma criada = plataformaService.salvar(plataforma);
        return ResponseEntity.ok(criada);
    }

    // ======================================================================================================
    // 3. BUSCAR PLATAFORMA POR ID
    // ======================================================================================================
    /**
     * Busca uma plataforma específica através do ID.
     *
     * @param id ID da plataforma
     * @return 200 OK caso encontrada, 404 Not Found caso não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plataforma> buscarPorId(@PathVariable Long id) {
        Plataforma plataforma = plataformaService.buscarPorId(id);

        if (plataforma == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(plataforma);
    }

    // ======================================================================================================
    // 4. ATUALIZAR PLATAFORMA
    // ======================================================================================================
    /**
     * Atualiza os dados de uma plataforma já existente.
     *
     * Regras:
     * • O ID deve existir.
     * • A lógica de atualização é tratada pelo service.
     *
     * @param id         ID da plataforma.
     * @param plataforma dados enviados para atualização.
     * @return plataforma atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Plataforma> atualizar(@PathVariable Long id,
            @RequestBody Plataforma plataforma) {

        Plataforma atualizada = plataformaService.atualizar(id, plataforma);
        return ResponseEntity.ok(atualizada);
    }

    // ======================================================================================================
    // 5. REMOVER PLATAFORMA
    // ======================================================================================================
    /**
     * Remove uma plataforma por ID.
     *
     * Regras:
     * • Caso o ID não exista, o service deve tratar e lançar exceção apropriada.
     *
     * @param id ID da plataforma a ser removida.
     * @return 204 No Content quando a remoção é realizada com sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        plataformaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}