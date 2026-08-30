package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.Admin;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AdminRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de usuários.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Responsabilidades: Orquestração de casos de uso para autenticação,
 * cadastro, atualização e gerenciamento de papéis (Admin, Professor, Perfil).
 * Padrões aplicados: Service Layer, Repository Pattern, BCrypt (segurança).
 * 
 * @see UsuarioService
 * @see docs/domain/identity_context.md
 * @see REQ-001 (Autenticação e Perfil de Usuário)
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Instância do encoder (pode ser injetado também)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Optional<Usuario> login(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verifica se a senha informada confere com a senha criptografada
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    @Override
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Verifica se o e-mail já está cadastrado
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Se for admin, cria como Admin
        if ("admin".equals(usuario.getEmail())) {
            Admin admin = new Admin();
            admin.setNome(usuario.getNome());
            admin.setEmail(usuario.getEmail());
            admin.setSenha(senhaCriptografada);
            admin.setSobrenome(usuario.getSobrenome());
            admin.setDataNascimento(usuario.getDataNascimento());
            admin.setTelefone(usuario.getTelefone());
            admin.setDescricao(usuario.getDescricao());
            admin.setInstituicaoEnsino(usuario.getInstituicaoEnsino());
            admin.setCep(usuario.getCep());
            admin.setPais(usuario.getPais());
            admin.setCidade(usuario.getCidade());
            admin.setSite(usuario.getSite());
            return adminRepository.save(admin);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

}