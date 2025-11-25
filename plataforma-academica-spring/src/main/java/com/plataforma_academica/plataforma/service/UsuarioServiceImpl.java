package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}