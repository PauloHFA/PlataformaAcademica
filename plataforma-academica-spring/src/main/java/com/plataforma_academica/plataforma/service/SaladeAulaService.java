package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.SaladeAula;

import java.util.List;

public interface SaladeAulaService {

    // --- Métodos de CRUD para a Sala de Aula ---

    SaladeAula criarSala(SaladeAula sala, Long criadorId);
    SaladeAula buscarSalaPorId(Long saladeAulaId);
    List<SaladeAula> listarTodasSalas();
    void deletarSala(Long saladeAulaId, Long userId); // Requer permissão do criador

    // --- Métodos de Gerenciamento de Membros (Usuário) ---

    // Adiciona um usuário à lista de membros (apenas o criador pode fazer isso)
    Usuario adicionarMembro(Long saladeAulaId, Long novoMembroId, Long creatorId);

    // Busca todos os membros de uma sala
    List<Usuario> listarMembros(Long saladeAulaId);

    // Remove um membro da sala (apenas o criador pode fazer isso)
    void removerMembro(Long saladeAulaId, Long membroId, Long creatorId);

    // --- Métodos de Gerenciamento de Atividades ---

    // Cadastra uma nova atividade (apenas o criador da sala pode fazer isso)
    Atividade cadastrarAtividade(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId);
    
    // Cadastra atividade com documento anexado
    Atividade cadastrarAtividadeComDocumento(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId, org.springframework.web.multipart.MultipartFile documento);

    // Busca uma atividade pelo ID
    Atividade buscarAtividadePorId(Long atividadeId);

    // Lista todas as atividades de uma sala
    List<Atividade> listarAtividadesPorSala(Long saladeAulaId);

    // Atualiza uma atividade (apenas o criador da sala pode fazer isso)
    Atividade atualizarAtividade(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId);

    // Deleta uma atividade (apenas o criador da sala pode fazer isso)
    void deletarAtividade(Long atividadeId, Long creatorId);
}