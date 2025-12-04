package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.SubmissaoAtividadeMapper;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.service.SubmissaoAtividadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ================================================================================
 *  SUBMISSAO ATIVIDADE CONTROLLER - Plataforma Acadêmica
 * ================================================================================
 *
 * Controlador responsável por gerenciar todo o fluxo de submissões de atividades
 * realizadas pelos alunos dentro da plataforma acadêmica.
 *
 * Este controller expõe endpoints REST que permitem:
 *
 *  1. Enviar submissão de atividade
 *     POST /api/submissaoatividade/atividade/{atividadeId}/aluno/{alunoId}
 *
 *  2. Listar todas as submissões de uma atividade (uso do professor)
 *     GET  /api/submissaoatividade/atividade/{atividadeId}
 *
 *  3. Buscar a submissão de um aluno específico para uma atividade
 *     GET  /api/submissaoatividade/atividade/{atividadeId}/aluno/{alunoId}
 *
 *  4. Corrigir submissão (atribuir nota + feedback)
 *     PUT  /api/submissaoatividade/corrigir/{submissaoId}
 *
 * --------------------------------------------------------------------------------
 *  RESPONSABILIDADES DO CONTROLLER
 * --------------------------------------------------------------------------------
 *  - Receber e responder requisições HTTP.
 *  - Delegar toda regra de negócio ao service.
 *  - Retornar códigos HTTP adequados.
 *
 * --------------------------------------------------------------------------------
 *  REGRAS DE NEGÓCIO (aplicadas no Service)
 * --------------------------------------------------------------------------------
 *  - Verificar se atividade existe.
 *  - Verificar se aluno existe e pertence à sala da atividade.
 *  - Impedir submissões duplicadas.
 *  - Registrar data de envio e correção.
 *  - Validar nota atribuída pelo professor.
 *
 * Este controller NÃO contém regras de negócio. Ele apenas transforma a requisição
 * em chamadas ao service e retorna a resposta adequada.
 *
 * ================================================================================
 */

@RestController
@RequestMapping("/api/submissaoatividade")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmissaoAtividadeController {

    private final SubmissaoAtividadeService submissaoService;

    public SubmissaoAtividadeController(SubmissaoAtividadeService submissaoService) {
        this.submissaoService = submissaoService;
    }

    // =========================================================================================
    //  1. ENVIAR SUBMISSÃO
    // =========================================================================================
    /**
     * Endpoint para envio da submissão de uma atividade por um aluno.
     *
     * Regras aplicadas no service:
     *  - Valida se a atividade existe.
     *  - Valida se o aluno existe.
     *  - Valida se o aluno pertence à sala onde a atividade está.
     *  - Verifica se o aluno já enviou submissão anteriormente.
     *
     * @param atividadeId ID da atividade sendo entregue
     * @param alunoId ID do aluno que está enviando a submissão
     * @param submissao objeto contendo dados da submissão
     * @return objeto SubmissaoAtividade salvo
     */
    @PostMapping("/atividade/{atividadeId}/aluno/{alunoId}")
    public ResponseEntity<SubmissaoAtividadeResponseDTO> enviarSubmissao(
            @PathVariable Long atividadeId,
            @PathVariable Long alunoId,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile arquivo) {

        SubmissaoAtividade enviada = submissaoService.enviarSubmissaoComArquivo(atividadeId, alunoId, descricao, arquivo);
        return ResponseEntity.ok(SubmissaoAtividadeMapper.toResponse(enviada));
    }

    // =========================================================================================
    //  2. LISTAR TODAS AS SUBMISSÕES DE UMA ATIVIDADE
    // =========================================================================================
    /**
     * Lista todas as submissões enviadas para uma atividade.
     *
     * Geralmente utilizado pelo professor para avaliar quais alunos entregaram
     * e acessar as respectivas submissões.
     *
     * @param atividadeId ID da atividade
     * @return lista de submissões
     */
    @GetMapping("/atividade/{atividadeId}")
    public ResponseEntity<List<SubmissaoAtividadeResponseDTO>> listarPorAtividade(@PathVariable Long atividadeId) {
        List<SubmissaoAtividadeResponseDTO> response = submissaoService.listarSubmissoesPorAtividade(atividadeId)
                .stream()
                .map(SubmissaoAtividadeMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // =========================================================================================
    //  3. BUSCAR SUBMISSÃO DE UM ALUNO
    // =========================================================================================
    /**
     * Busca a submissão enviada por um aluno específico para uma atividade.
     *
     * @param atividadeId ID da atividade
     * @param alunoId ID do aluno
     * @return submissão do aluno, caso exista
     */
    @GetMapping("/atividade/{atividadeId}/aluno/{alunoId}")
    public ResponseEntity<SubmissaoAtividadeResponseDTO> buscarSubmissaoAluno(
            @PathVariable Long atividadeId,
            @PathVariable Long alunoId) {

        SubmissaoAtividade sub = submissaoService.buscarSubmissaoDoAluno(atividadeId, alunoId);
        return ResponseEntity.ok(SubmissaoAtividadeMapper.toResponse(sub));
    }

    // =========================================================================================
    //  4. CORRIGIR SUBMISSÃO
    // =========================================================================================
    /**
     * Permite que o professor atribua uma nota e um feedback para a submissão de um aluno.
     *
     * Regras tratadas no service:
     * - Verifica se a submissão existe
     * - Valida nota (ex.: 0 a 10)
     * - Define data de correção
     *
     * @param submissaoId ID da submissão a ser corrigida
     * @param nota nota atribuída ao aluno
     * @param feedback texto opcional do professor com observações
     * @return submissão corrigida
     */
    @PutMapping("/corrigir/{submissaoId}")
    public ResponseEntity<SubmissaoAtividadeResponseDTO> corrigirSubmissao(
            @PathVariable Long submissaoId,
            @RequestParam Double nota,
            @RequestParam(required = false) String feedback) {

        SubmissaoAtividade corrigida = submissaoService.corrigirSubmissao(submissaoId, nota, feedback);
        return ResponseEntity.ok(SubmissaoAtividadeMapper.toResponse(corrigida));
    }
}
