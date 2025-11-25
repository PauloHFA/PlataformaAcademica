package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ComunidadeRepository;
import com.plataforma_academica.plataforma.repository.MembroComunidadeRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComunidadeServiceImpl implements ComunidadeService {

    private final ComunidadeRepository comunidadeRepository;
    private final MembroComunidadeRepository membroRepo;
    private final UsuarioRepository usuarioRepo;

    public ComunidadeServiceImpl(ComunidadeRepository comunidadeRepository,
                                 MembroComunidadeRepository membroRepo,
                                 UsuarioRepository usuarioRepo) {
        this.comunidadeRepository = comunidadeRepository;
        this.membroRepo = membroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    @Transactional
    public Comunidade criarComunidade(@Valid ComunidadeDTO comunidadeDTO) {
        Usuario dono = usuarioRepo.findById(comunidadeDTO.getDonoId())
                .orElseThrow(() -> new ResourceNotFoundException("Dono não encontrado"));
        Comunidade c = new Comunidade();
        c.setNome(comunidadeDTO.getNome());
        c.setDescricao(comunidadeDTO.getDescricao());
        c.setDono(dono);
        Comunidade saved = comunidadeRepository.save(c);

        // adicionar dono como membro com papel ADMIN
        MembroComunidade m = new MembroComunidade();
        m.setComunidade(saved);
        m.setUsuario(dono);
        m.setPapel("ADMIN");
        membroRepo.save(m);

        return saved;
    }

    @Override
    public void deletarComunidade(Long id, Long solicitanteId) {
        Comunidade c = comunidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade não encontrada"));
        if (!c.getDono().getId().equals(solicitanteId)) {
            throw new BadRequestException("Apenas o dono pode deletar a comunidade.");
        }
        comunidadeRepository.delete(c);
    }

    @Override
    public MembroComunidade entrarComunidade(Long comunidadeId, Long usuarioId) {
        Comunidade c = comunidadeRepository.findById(comunidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade não encontrada"));
        Usuario u = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        membroRepo.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId).ifPresent(x -> {
            throw new BadRequestException("Usuário já é membro");
        });
        MembroComunidade m = new MembroComunidade();
        m.setComunidade(c);
        m.setUsuario(u);
        m.setPapel("MEMBRO");
        return membroRepo.save(m);
    }

    @Override
    public void sairComunidade(Long comunidadeId, Long usuarioId) {
        MembroComunidade m = membroRepo.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Associação não encontrada"));
        // se for dono e único admin, poderia proibir — lógica simplificada: permite sair
        membroRepo.delete(m);
    }

    @Override
    public List<Comunidade> listarTodas() {
        return comunidadeRepository.findAll();
    }

    @Override
    public List<MembroComunidade> listarMembros(Long comunidadeId) {
        return membroRepo.findByComunidadeId(comunidadeId);
    }
}
