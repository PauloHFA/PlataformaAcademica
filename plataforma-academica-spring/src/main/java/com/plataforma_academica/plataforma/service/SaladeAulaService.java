package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.SaladeAula;

import java.util.List;

/**
 * Interface do serviço de Salas de Aula.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see SaladeAula
 * @see REQ-018 (Criação de Salas de Aula)
 */
/**
 * Interface do serviço de Salas de Aula.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see SaladeAula
 * @see REQ-018 (Criação de Salas de Aula)
 */
public interface SaladeAulaService {

    // --- Métodos de CRUD para a Sala de Aula ---

    SaladeAula criarSala(SaladeAula sala, UUID criadorId);

    SaladeAula buscarSalaPorId(UUID saladeAulaId);

    List<SaladeAula> listarTodasSalas();

    void deletarSala(UUID saladeAulaId, UUID userId); // Requer permissão do criador

    // --- Métodos de Gerenciamento de Membros (Usuário) ---

    // Adiciona um usuário à lista de membros (apenas o criador pode fazer isso)
    Usuario adicionarMembro(UUID saladeAulaId, UUID novoMembroId, UUID creatorId);

    // Busca todos os membros de uma sala
    List<Usuario> listarMembros(UUID saladeAulaId);

    // Remove um membro da sala (apenas o criador pode fazer isso)
    void removerMembro(UUID saladeAulaId, UUID membroId, UUID creatorId);

    // --- Métodos de Gerenciamento de Atividades ---

    // Cadastra uma nova atividade (apenas o criador da sala pode fazer isso)
    Atividade cadastrarAtividade(UUID saladeAulaId, AtividadeDTO atividadeDTO, UUID creatorId);

    // Cadastra atividade com documento anexado
    Atividade cadastrarAtividadeComDocumento(UUID saladeAulaId, AtividadeDTO atividadeDTO, UUID creatorId,
            org.springframework.web.multipart.MultipartFile documento);

    // Busca uma atividade pelo ID
    Atividade buscarAtividadePorId(UUID atividadeId);

    // Lista todas as atividades de uma sala
    List<Atividade> listarAtividadesPorSala(UUID saladeAulaId);

    // Atualiza uma atividade (apenas o criador da sala pode fazer isso)
    Atividade atualizarAtividade(UUID saladeAulaId, AtividadeDTO atividadeDTO, UUID creatorId);

    // Deleta uma atividade (apenas o criador da sala pode fazer isso)
    void deletarAtividade(UUID atividadeId, UUID creatorId);
}