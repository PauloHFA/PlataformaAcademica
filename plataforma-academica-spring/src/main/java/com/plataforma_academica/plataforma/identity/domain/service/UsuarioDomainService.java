package com.plataforma_academica.plataforma.identity.domain.service;

import com.plataforma_academica.plataforma.identity.domain.model.Email;
import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import com.plataforma_academica.plataforma.identity.domain.model.SenhaHash;
import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import com.plataforma_academica.plataforma.identity.domain.port.PasswordHasherPort;
import com.plataforma_academica.plataforma.identity.domain.repository.UsuarioRepository;

/**
 * Serviço de domínio para regras de negócio que envolvem múltiplos agregados
 * ou validações complexas de unicidade.
 */
public class UsuarioDomainService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordHasherPort passwordHasher;

    public UsuarioDomainService(UsuarioRepository usuarioRepository, PasswordHasherPort passwordHasher) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHasher = passwordHasher;
    }

    public Usuario registrarNovoUsuario(String email, String senhaEmTextoPuro, String nome, Papel papel) {
        Email emailVO = Email.de(email);

        if (usuarioRepository.existsByEmail(emailVO)) {
            throw new EmailJaCadastradoException("E-mail já cadastrado: " + email);
        }

        SenhaHash senhaHash = passwordHasher.hash(senhaEmTextoPuro);
        return Usuario.cadastrar(emailVO, senhaHash, nome, papel);
    }

    public void alterarSenha(Usuario usuario, String senhaAtual, String novaSenha) {
        if (!passwordHasher.matches(senhaAtual, usuario.senhaHash())) {
            throw new SenhaInvalidaException("Senha atual incorreta");
        }
        SenhaHash novoHash = passwordHasher.hash(novaSenha);
        usuario.alterarSenha(novoHash);
    }

    public static class EmailJaCadastradoException extends RuntimeException {
        public EmailJaCadastradoException(String message) {
            super(message);
        }
    }

    public static class SenhaInvalidaException extends RuntimeException {
        public SenhaInvalidaException(String message) {
            super(message);
        }
    }
}