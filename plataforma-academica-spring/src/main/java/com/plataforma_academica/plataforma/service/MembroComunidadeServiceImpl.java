package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.repository.MembroComunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço de Membros de Comunidades.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see MembroComunidadeService
 * @see docs/domain/social_context.md
 * @see REQ-016 (Gestão de Membros em Comunidades)
 */
@Service
public class MembroComunidadeServiceImpl implements MembroComunidadeService {

    @Autowired
    private MembroComunidadeRepository membroComunidadeRepository;

    /**
     * Salva ou atualiza a associação de um membro a uma comunidade.
     * 
     * @param membro Entidade MembroComunidade a ser persistida.
     * @return Instância salva.
     */
    @Override
    public MembroComunidade salvar(MembroComunidade membro) {
        // Passo 1: Persistir associação no repositório
        return membroComunidadeRepository.save(membro);
    }

    /**
     * Busca uma associação de membro pelo ID.
     * 
     * @param id ID do membro da comunidade.
     * @return Instância encontrada ou null se não existir.
     */
    @Override
    public MembroComunidade buscarPorId(UUID id) {
        // Passo 1: Consultar banco de dados por ID
        return membroComunidadeRepository.findById(id).orElse(null);
    }

    @Override
    public List<MembroComunidade> listarTodos() {
        return membroComunidadeRepository.findAll();
    }

    @Override
    public void deletar(UUID id) {
        membroComunidadeRepository.deleteById(id);
    }

    @Override
    public List<MembroComunidade> buscarPorComunidade(UUID comunidadeId) {
        return membroComunidadeRepository.findByComunidadeId(comunidadeId);
    }

    @Override
    public List<MembroComunidade> buscarPorUsuario(UUID usuarioId) {
        return membroComunidadeRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public MembroComunidade buscarPorUsuarioEComunidade(UUID usuarioId, UUID comunidadeId) {
        return membroComunidadeRepository
                .findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)
                .orElse(null);
    }
}
