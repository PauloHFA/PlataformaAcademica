package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos vínculos entre
 * usuários e comunidades.
 */
public interface MembroComunidadeService {

    /** Salva um novo membro ou atualiza um existente. */
    MembroComunidade salvar(MembroComunidade membro);

    /** Busca vínculo por ID. */
    MembroComunidade buscarPorId(UUID id);

    /** Retorna todos os vínculos cadastrados. */
    List<MembroComunidade> listarTodos();

    /** Remove vínculo pelo ID. */
    void deletar(UUID id);

    /** Retorna todos os vínculos de uma comunidade. */
    List<MembroComunidade> buscarPorComunidade(UUID comunidadeId);

    /** Retorna todos os vínculos de um usuário. */
    List<MembroComunidade> buscarPorUsuario(UUID usuarioId);

    /** Busca um vínculo específico entre usuário e comunidade. */
    MembroComunidade buscarPorUsuarioEComunidade(UUID usuarioId, UUID comunidadeId);
}
