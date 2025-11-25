package com.plataforma_academica.plataforma.service;

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
    MembroComunidade buscarPorId(Long id);

    /** Retorna todos os vínculos cadastrados. */
    List<MembroComunidade> listarTodos();

    /** Remove vínculo pelo ID. */
    void deletar(Long id);

    /** Retorna todos os vínculos de uma comunidade. */
    List<MembroComunidade> buscarPorComunidade(Long comunidadeId);

    /** Retorna todos os vínculos de um usuário. */
    List<MembroComunidade> buscarPorUsuario(Long usuarioId);

    /** Busca um vínculo específico entre usuário e comunidade. */
    MembroComunidade buscarPorUsuarioEComunidade(Long usuarioId, Long comunidadeId);
}
