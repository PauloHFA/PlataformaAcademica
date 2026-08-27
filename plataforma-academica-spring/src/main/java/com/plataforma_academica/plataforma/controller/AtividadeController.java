package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.dto.AtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.AtividadeMapper;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.service.AtividadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller REST responsável pela gestão de Atividades acadêmicas.
 * 
 * Camada: Presentation / REST Controller (Academic Context)
 * Contexto de Negócio: Tarefas, exercícios e materiais avaliativos criados por
 * professores em salas de aula.
 * Padrões aplicados: RestController, CrossOrigin, DTOs.
 * 
 * @see AtividadeService
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Criação de Atividades)
 */
@RestController
@RequestMapping("/atividades")
@CrossOrigin(origins = "http://localhost:4200")
public class AtividadeController {
    **
   As atividades representam:*•Tarefas*•Exercícios*•
    Conteúdos postados
    pelo professor*•
    Materiais avaliativos
    ou informativos**
    Este controller
    serve como
    camada REST
    entre o
    frontend e
    o AtividadeService,*
    fornecendo endpoints
    para criação, edição, consulta
    e exclusão.**
   Funcionalidades disponíveis:*POST/atividades/sala/{salaId}/autor/{autorId}→
    Criar atividade
    na sala*GET/atividades/{atividadeId}→
    Buscar atividade
    por ID*GET/atividades/sala/{salaId}→
    Listar atividades
    da sala*GET/atividades/autor/{autorId}→
    Listar atividades
    criadas por
    um professor*PUT/atividades/{atividadeId}/autor/{autorId}→
    Atualizar atividade*DELETE/atividades/{atividadeId}/autor/{autorId}→
    Deletar atividade**
    Regras e
    validações importantes:*•
    Apenas o
    criador da
    sala tem
    permissão para criar,
    editar ou
    apagar atividades.*•
    Toda validação
    de segurança, permissão
    e integridade
    é feita
    exclusivamente no AtividadeService.*•
    Este controller
    apenas recebe dados,
    delega ao
    service e
    retorna uma
    resposta HTTP adequada.**Objetivo:*
    Garantir que
    a comunicação
    com o
    frontend seja clara,
    segura e padronizada,*
    utilizando DTOs
    e responses organizadas.**
   @see AtividadeService
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Criação de Atividades)*/

    @RestController
    @RequestMapping("/atividades")
    @CrossOrigin(origins = "http://localhost:4200")
    public class AtividadeController {

        private final AtividadeService atividadeService;

        public AtividadeController(AtividadeService atividadeService) {
            this.atividadeService = atividadeService;
        }

        // ========================================================================
        // 1. CRIAR ATIVIDADE
        // ========================================================================
        /**
         * Cria uma nova atividade associada a uma sala de aula.
         *
         * Regras:
         * - Apenas o criador da sala pode criar atividades.
         * - O service verifica:
         * • Se a sala existe
         * • Se o autor existe
         * • Se o autor realmente é o criador da sala
         *
         * @param salaId  ID da sala onde a atividade será criada
         * @param autorId ID do usuário criador da sala
         * @param dto     Dados da atividade enviada pelo cliente
         * @return A atividade criada
         */
        @PostMapping("/sala/{salaId}/autor/{autorId}")
        public ResponseEntity<AtividadeResponseDTO> criarAtividade(
                @PathVariable Long salaId,
                @PathVariable Long autorId,
                @RequestBody AtividadeDTO dto) {
            Atividade atividadeCriada = atividadeService.criarAtividade(salaId, dto, autorId);
            return ResponseEntity.ok(AtividadeMapper.toResponse(atividadeCriada));
        }

        // ========================================================================
        // 2. BUSCAR ATIVIDADE POR ID
        // ========================================================================
        /**
         * Retorna os dados de uma atividade específica.
         *
         * Caso não exista, o service lança exceção apropriada.
         *
         * @param atividadeId ID da atividade
         * @return A atividade encontrada
         */
        @GetMapping("/{atividadeId}")
        public ResponseEntity<AtividadeResponseDTO> buscarAtividadePorId(
                @PathVariable Long atividadeId) {
            Atividade atividade = atividadeService.buscarAtividadePorId(atividadeId);
            return ResponseEntity.ok(AtividadeMapper.toResponse(atividade));
        }

        // ========================================================================
        // 3. LISTAR ATIVIDADES POR SALA
        // ========================================================================
        /**
         * Lista todas as atividades associadas a uma sala.
         *
         * Ideal para:
         * - Dashboard do professor
         * - Visualização dos alunos
         *
         * @param salaId ID da sala
         * @return Lista contendo atividades da sala
         */
        @GetMapping("/sala/{salaId}")
        public ResponseEntity<List<AtividadeResponseDTO>> listarPorSala(
                @PathVariable Long salaId) {
            List<Atividade> lista = atividadeService.listarAtividadesPorSala(salaId);
            return ResponseEntity.ok(lista.stream().map(AtividadeMapper::toResponse).toList());
        }

        // ========================================================================
        // 4. LISTAR ATIVIDADES POR AUTOR
        // ========================================================================
        /**
         * Lista todas as atividades criadas por um determinado professor/autor.
         *
         * Usado em:
         * - Tela "Minhas Atividades"
         *
         * @param autorId ID do autor
         * @return Lista de atividades criadas pelo autor
         */
        @GetMapping("/autor/{autorId}")
        public ResponseEntity<List<AtividadeResponseDTO>> listarPorAutor(
                @PathVariable Long autorId) {
            List<Atividade> lista = atividadeService.listarAtividadesPorAutor(autorId);
            return ResponseEntity.ok(lista.stream().map(AtividadeMapper::toResponse).toList());
        }

        // ========================================================================
        // 5. ATUALIZAR ATIVIDADE
        // ========================================================================
        /**
         * Atualiza dados de uma atividade existente.
         *
         * Regras:
         * - Apenas o criador da sala pode atualizar atividades.
         * - O service valida:
         * • Se a atividade existe
         * • Se o autor existe
         * • Se o autor tem permissão de edição
         *
         * @param atividadeId         ID da atividade
         * @param autorId             ID do autor solicitante
         * @param atividadeAtualizada Dados novos enviados pelo cliente
         * @return A atividade após atualização
         */
        @PutMapping("/{atividadeId}/autor/{autorId}")
        public ResponseEntity<AtividadeResponseDTO> atualizarAtividade(
                @PathVariable Long atividadeId,
                @PathVariable Long autorId,
                @RequestBody AtividadeDTO dto) {
            Atividade atualizada = atividadeService.atualizarAtividade(atividadeId, dto, autorId);
            return ResponseEntity.ok(AtividadeMapper.toResponse(atualizada));
        }

        // ========================================================================
        // 6. DELETAR ATIVIDADE
        // ========================================================================
        /**
         * Remove definitivamente uma atividade.
         *
         * Apenas o criador da sala tem permissão para excluir.
         *
         * @param atividadeId ID da atividade
         * @param autorId     ID do autor solicitante
         * @return No Content (204) em caso de sucesso
         */
        @DeleteMapping("/{atividadeId}/autor/{autorId}")
        public ResponseEntity<Void> deletarAtividade(
                @PathVariable Long atividadeId,
                @PathVariable Long autorId) {
            atividadeService.deletarAtividade(atividadeId, autorId);
            return ResponseEntity.noContent().build();
        }
}