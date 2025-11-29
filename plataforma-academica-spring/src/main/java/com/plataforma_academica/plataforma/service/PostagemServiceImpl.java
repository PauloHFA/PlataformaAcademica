package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;
import com.plataforma_academica.plataforma.mapper.PostagemMapper;
import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.model.Postagem;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
import com.plataforma_academica.plataforma.repository.PostagemRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostagemServiceImpl implements PostagemService {

    @Autowired
    PostagemRepository postagemRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PlataformaRepository plataformaRepository;

    @Override
    public PostagemDTO publicar(PostagemDTO dto) {

        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Plataforma plataforma = plataformaRepository.findById(dto.getPlataformaId())
                .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));

        Postagem postagem = PostagemMapper.toEntity(dto, autor, plataforma);
        return PostagemMapper.toDTO(postagemRepository.save(postagem));
    }

    @Override
    public List<PostagemDTO> listarTodas() {
        return postagemRepository.findAll()
                .stream()
                .map(PostagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemDTO buscarPorId(Long id) {
        return postagemRepository.findById(id)
                .map(PostagemMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<PostagemDTO> buscarPorTitulo(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(PostagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemDTO atualizar(PostagemDTO dto) {
        return publicar(dto);
    }

    @Override
    public void deletar(Long id) {
        postagemRepository.deleteById(id);
    }

    @Override
    public PostagemResponseDTO publicarResponse(PostagemDTO dto) {
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Plataforma plataforma = plataformaRepository.findById(dto.getPlataformaId())
                .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));

        Postagem postagem = PostagemMapper.toEntity(dto, autor, plataforma);
        return PostagemMapper.toResponse(postagemRepository.save(postagem));
    }

    @Override
    public List<PostagemResponseDTO> listarTodasResponse() {
        return postagemRepository.findAll()
                .stream()
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemResponseDTO buscarPorIdResponse(Long id) {
        return postagemRepository.findById(id)
                .map(PostagemMapper::toResponse)
                .orElse(null);
    }

    @Override
    public List<PostagemResponseDTO> buscarPorTituloResponse(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemResponseDTO atualizarResponse(PostagemDTO dto) {
        return publicarResponse(dto);
    }
}
