package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ArtigoRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final ArtigoRepository artigoRepo;
    private final UsuarioRepository usuarioRepo;

    public ArtigoServiceImpl(ArtigoRepository artigoRepo, UsuarioRepository usuarioRepo) {
        this.artigoRepo = artigoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Artigo criar(ArtigoDTO dto) {
        Usuario autor = usuarioRepo.findById(dto.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

        Artigo artigo = new Artigo();
        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());
        artigo.setAutor(autor);

        return artigoRepo.save(artigo);
    }

    @Override
    @Transactional
    public Artigo editar(Long id, ArtigoDTO dto) {
        Artigo artigo = artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        if (!artigo.getAutor().getId().equals(dto.getAutorId())) {
            throw new BadRequestException("Apenas o autor pode editar o artigo.");
        }

        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());
        artigo.setAtualizadoEm(LocalDateTime.now());

        return artigoRepo.save(artigo);
    }

    @Override
    public void deletar(Long id, Long solicitanteId) {
        Artigo artigo = artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        if (!artigo.getAutor().getId().equals(solicitanteId)) {
            throw new BadRequestException("Apenas o autor pode deletar o artigo.");
        }

        artigoRepo.delete(artigo);
    }

    @Override
    public Artigo buscarPorId(Long id) {
        return artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));
    }

    @Override
    public List<Artigo> listarTodos() {
        return artigoRepo.findAll();
    }

    @Override
    public List<Artigo> listarPorAutor(Long autorId) {
        return artigoRepo.findByAutorId(autorId);
    }
}
