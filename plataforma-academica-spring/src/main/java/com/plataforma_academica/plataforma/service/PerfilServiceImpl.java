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

/**
 * Implementação do serviço de gerenciamento de perfis de usuário.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Orquestra operações de criação, atualização com controle de concorrência
 * (isolamento SERIALIZABLE) e consultas de perfil por ID, curso ou usuário.
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional
 * (SERIALIZABLE).
 * 
 * @see PerfilService
 * @see docs/domain/identity_context.md
 * @see REQ-002 (Perfil Acadêmico)
 */
@Service
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;

    public PerfilServiceImpl(PerfilRepository perfilRepository,
            UsuarioRepository usuarioRepository) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Salva ou atualiza um perfil de usuário a partir de um DTO.
     * Utiliza isolamento SERIALIZABLE para evitar condições de corrida
     * (concorrência).
     * 
     * @param dto DTO contendo os dados do perfil.
     * @return Entidade Perfil salva.
     * @throws RuntimeException se o usuário associado não for encontrado.
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Perfil salvar(PerfilDTO dto) {
        // Passo 1: Validar existência do usuário base
        if (!usuarioRepository.existsById(dto.getUsuarioId())) {
            throw new RuntimeException("Usuário não encontrado com ID: " + dto.getUsuarioId());
        }

        // Passo 2: Buscar perfil existente ou inicializar novo
        Perfil perfil = perfilRepository.findById(dto.getUsuarioId())
                .orElse(null);

        // Passo 3: Mapear DTO para entidade (criação ou atualização)
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
            if (dto.getNome() != null)
                perfil.setNome(dto.getNome());
            if (dto.getSobrenome() != null)
                perfil.setSobrenome(dto.getSobrenome());
            if (dto.getEmail() != null)
                perfil.setEmail(dto.getEmail());
            if (dto.getInstituicaoEnsino() != null)
                perfil.setInstituicaoEnsino(dto.getInstituicaoEnsino());
            if (dto.getCep() != null)
                perfil.setCep(dto.getCep());
            if (dto.getPais() != null)
                perfil.setPais(dto.getPais());
            if (dto.getCidade() != null)
                perfil.setCidade(dto.getCidade());
            if (dto.getSite() != null)
                perfil.setSite(dto.getSite());
            if (dto.getTelefone() != null)
                perfil.setTelefone(dto.getTelefone());
            if (dto.getDataNascimento() != null) {
                perfil.setDataNascimento(java.time.LocalDate.parse(dto.getDataNascimento()));
            }
            if (dto.getDescricao() != null)
                perfil.setDescricao(dto.getDescricao());
        }

        // Passo 4: Persistir e retornar
        return perfilRepository.save(perfil);
    }

    /**
     * Atualiza especificamente a biografia, foto e curso de um perfil existente.
     * 
     * @param id  ID do perfil.
     * @param dto DTO com novos dados.
     * @return Perfil atualizado.
     * @throws RuntimeException se o perfil não for encontrado.
     */
    @Override
    @Transactional
    public Perfil atualizar(Long id, PerfilDTO dto) {
        // Passo 1: Localizar perfil ou falhar
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // Passo 2: Atualizar campos permitidos
        perfil.setBio(dto.getBio());
        perfil.setFotoPerfil(dto.getFotoPerfil());
        perfil.setCurso(dto.getCurso());

        // Passo 3: Salvar alterações
        return perfilRepository.save(perfil);
    }

    /**
     * Lista todos os perfis cadastrados no sistema.
     * 
     * @return Lista de perfis.
     */
    @Override
    public List<Perfil> listarTodos() {
        return perfilRepository.findAll();
    }

    /**
     * Busca um perfil pelo ID correspondente.
     * 
     * @param id ID do perfil.
     * @return Perfil encontrado ou null.
     */
    @Override
    public Perfil buscarPorId(Long id) {
        return perfilRepository.findById(id).orElse(null);
    }

    /**
     * Busca perfis associados a um determinado curso acadêmico.
     * 
     * @param curso Nome do curso.
     * @return Lista de perfis do curso.
     */
    @Override
    public List<Perfil> buscarPorCurso(String curso) {
        return perfilRepository.findByCurso(curso);
    }

    /**
     * Busca o perfil de um usuário específico pelo ID do usuário.
     * 
     * @param usuarioId ID do usuário.
     * @return Perfil encontrado ou null.
     */
    @Override
    public Perfil buscarPorUsuarioId(Long usuarioId) {
        return perfilRepository.findById(usuarioId).orElse(null);
    }

    /**
     * Verifica se já existe um perfil cadastrado para o usuário.
     * 
     * @param usuarioId ID do usuário.
     * @return true se existir, false caso contrário.
     */
    @Override
    public boolean existePerfilDoUsuario(Long usuarioId) {
        return perfilRepository.existsById(usuarioId);
    }
}
