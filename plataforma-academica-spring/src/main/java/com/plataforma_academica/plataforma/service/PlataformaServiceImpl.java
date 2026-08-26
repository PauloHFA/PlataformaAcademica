package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação do serviço de plataforma.
 * 
 * Camada: Application Service
 * Responsabilidades: Gerenciar configurações e recursos da plataforma
 * acadêmica.
 */
/**
 * Implementação do serviço de Plataforma.
 * 
 * Camada: Application / Business Service
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see PlataformaService
 * @see REQ-001 (Configuração da Plataforma)
 */
@Service
public class PlataformaServiceImpl implements PlataformaService {

    private final PlataformaRepository plataformaRepository;

    public PlataformaServiceImpl(PlataformaRepository plataformaRepository) {
        this.plataformaRepository = plataformaRepository;
    }

    @Override
    public Plataforma salvar(Plataforma plataforma) {
        return plataformaRepository.save(plataforma);
    }

    @Override
    public Plataforma atualizar(Long id, Plataforma plataformaAtualizada) {
        Plataforma existente = plataformaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));

        existente.setNome(plataformaAtualizada.getNome());
        existente.setNome(plataformaAtualizada.getNome());

        return plataformaRepository.save(existente);
    }

    @Override
    public Plataforma buscarPorId(Long id) {
        return plataformaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Plataforma> listarTudo() {
        return plataformaRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        plataformaRepository.deleteById(id);
    }
}
