package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.mapper.PerfilMapper;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.PerfilRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Perfil salvar(PerfilDTO dto) {

        if (!usuarioRepository.existsById(dto.getUsuarioId())) {
            throw new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId());
        }

        Perfil perfil = perfilRepository.findById(dto.getUsuarioId())
                .orElse(null);

        if (perfil == null) {
            perfil = PerfilMapper.toEntity(dto);
            perfil.setId(dto.getUsuarioId()); // id do perfil é o id do usuario
            perfil.setVersion(0L); // inicializar version para novo perfil
        } else {
            // atualizar campos extras e usuario
            perfil.setBio(dto.getBio());
            perfil.setCurso(dto.getCurso());
            perfil.setFotoPerfil(dto.getFotoPerfil());
            // campos de usuario podem ser atualizados se fornecidos
            if (dto.getNome() != null) perfil.setNome(dto.getNome());
            if (dto.getSobrenome() != null) perfil.setSobrenome(dto.getSobrenome());
            if (dto.getEmail() != null) perfil.setEmail(dto.getEmail());
            if (dto.getInstituicaoEnsino() != null) perfil.setInstituicaoEnsino(dto.getInstituicaoEnsino());
            if (dto.getCep() != null) perfil.setCep(dto.getCep());
            if (dto.getPais() != null) perfil.setPais(dto.getPais());
            if (dto.getCidade() != null) perfil.setCidade(dto.getCidade());
            if (dto.getSite() != null) perfil.setSite(dto.getSite());
            if (dto.getTelefone() != null) perfil.setTelefone(dto.getTelefone());
            if (dto.getDataNascimento() != null) {
                perfil.setDataNascimento(java.time.LocalDate.parse(dto.getDataNascimento()));
            }
            if (dto.getDescricao() != null) perfil.setDescricao(dto.getDescricao());
        }

        return perfilRepository.save(perfil);
    }

    @Override
    @Transactional
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
        return perfilRepository.findById(usuarioId).orElse(null);
    }

    @Override
    public boolean existePerfilDoUsuario(Long usuarioId) {
        return perfilRepository.existsById(usuarioId);
    }
}
