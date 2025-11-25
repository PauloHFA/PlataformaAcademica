package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.PerfilRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilServiceImpl(PerfilRepository perfilRepository,
                             UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Perfil salvar(PerfilDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Perfil perfil = new Perfil();
        perfil.setBio(dto.getBio());
        perfil.setFotoPerfil(dto.getFotoPerfil());
        perfil.setCurso(dto.getCurso());
        perfil.setUsuario(usuario);

        return perfilRepository.save(perfil);
    }

    @Override
    public Perfil atualizar(Long id, PerfilDTO dto) {

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        perfil.setBio(dto.getBio());
        perfil.setFotoPerfil(dto.getFotoPerfil());
        perfil.setCurso(dto.getCurso());

        return perfilRepository.save(perfil);
    }

    @Override
    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }

    @Override
    public Perfil buscarPorId(Long id) {
        return perfilRepository.findById(id).orElse(null);
    }

    @Override
    public List<Perfil> buscarPorCurso(String curso) {
        return perfilRepository.findByCurso(curso);
    }

    @Override
    public Perfil buscarPorUsuarioId(Long usuarioId) {
        return perfilRepository.findByUsuarioId(usuarioId)
                .stream().findFirst().orElse(null);
    }

    @Override
    public boolean existePerfilDoUsuario(Long usuarioId) {
        return perfilRepository.existsByUsuarioId(usuarioId);
    }
}
