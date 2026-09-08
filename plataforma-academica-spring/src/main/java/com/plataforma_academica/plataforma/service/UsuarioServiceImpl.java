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
import java.time.LocalDateTime;
import java.util.UUID;

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
    public Usuario loginSocial(String email, String nome) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(email);
        if (existente.isPresent()) {
            Usuario usuario = existente.get();
            usuario.setEmailConfirmado(true);
            usuario.setTokenConfirmacaoEmail(null);
            usuario.setTokenConfirmacaoExpiraEm(null);
            return usuarioRepository.save(usuario);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome == null || nome.isBlank() ? email.split("@")[0] : nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
        usuario.setSenhaHash(usuario.getSenha());
        usuario.setEmailConfirmado(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario confirmarEmail(String token) {
        Usuario usuario = usuarioRepository.findAll().stream()
                .filter(item -> token.equals(item.getTokenConfirmacaoEmail()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Token de confirmação inválido"));
        if (usuario.getTokenConfirmacaoExpiraEm() == null
                || usuario.getTokenConfirmacaoExpiraEm().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token de confirmação expirado");
        }
        usuario.setEmailConfirmado(true);
        usuario.setTokenConfirmacaoEmail(null);
        usuario.setTokenConfirmacaoExpiraEm(null);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> login(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verifica se a senha informada confere com a senha armazenada
            // Suporta tanto BCrypt quanto texto plano (para seed/testes)
            boolean senhaCorreta = false;
            String storedPassword = usuario.getSenha() != null ? usuario.getSenha() : usuario.getSenhaHash();
            if (storedPassword != null && storedPassword.startsWith("$2a$")) {
                senhaCorreta = passwordEncoder.matches(senha, storedPassword);
            } else if (storedPassword != null) {
                senhaCorreta = senha.equals(storedPassword);
            }
            if (senhaCorreta) {
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
        usuario.setSenhaHash(senhaCriptografada);
        usuario.setEmailConfirmado(false);
        usuario.setTokenConfirmacaoEmail(UUID.randomUUID().toString());
        usuario.setTokenConfirmacaoExpiraEm(LocalDateTime.now().plusHours(24));

        // Se for admin, cria como Admin
        if ("admin".equals(usuario.getEmail())) {
            Admin admin = new Admin();
            admin.setNome(usuario.getNome());
            admin.setEmail(usuario.getEmail());
            admin.setSenha(senhaCriptografada);
            admin.setSenhaHash(senhaCriptografada);
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