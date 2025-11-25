package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.repository.MembroComunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço de MembroComunidade.
 *
 * Centraliza as operações de acesso ao banco de dados e garante a
 * comunicação entre o controller e o repository.
 */
@Service
public class MembroComunidadeServiceImpl implements MembroComunidadeService {

    @Autowired
    private MembroComunidadeRepository membroComunidadeRepository;

    @Override
    public MembroComunidade salvar(MembroComunidade membro) {
        return membroComunidadeRepository.save(membro);
    }

    @Override
    public MembroComunidade buscarPorId(Long id) {
        return membroComunidadeRepository.findById(id).orElse(null);
    }

    @Override
    public List<MembroComunidade> listarTodos() {
        return membroComunidadeRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        membroComunidadeRepository.deleteById(id);
    }

    @Override
    public List<MembroComunidade> buscarPorComunidade(Long comunidadeId) {
        return membroComunidadeRepository.findByComunidadeId(comunidadeId);
    }

    @Override
    public List<MembroComunidade> buscarPorUsuario(Long usuarioId) {
        return membroComunidadeRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public MembroComunidade buscarPorUsuarioEComunidade(Long usuarioId, Long comunidadeId) {
        return membroComunidadeRepository
                .findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)
                .orElse(null);
    }
}
