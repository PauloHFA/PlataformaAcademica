package com.plataforma_academica.plataforma.identity.application.command;

import com.plataforma_academica.plataforma.identity.domain.event.UsuarioCadastradoEvent;
import com.plataforma_academica.plataforma.identity.domain.model.Email;
import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import com.plataforma_academica.plataforma.identity.domain.model.SenhaHash;
import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import com.plataforma_academica.plataforma.identity.domain.repository.UsuarioRepository;
import com.plataforma_academica.plataforma.identity.domain.service.UsuarioDomainService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handler para o comando de cadastro de usuário.
 * Orquestra a criação, persistência e publicação do evento de domínio.
 */
@Component
public class CadastrarUsuarioCommandHandler {

    private final UsuarioDomainService usuarioDomainService;
    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CadastrarUsuarioCommandHandler(UsuarioDomainService usuarioDomainService,
            UsuarioRepository usuarioRepository,
            ApplicationEventPublisher eventPublisher) {
        this.usuarioDomainService = usuarioDomainService;
        this.usuarioRepository = usuarioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Usuario handle(CadastrarUsuarioCommand command) {
        // 1. Executa regra de negócio (validação de unicidade + hash de senha)
        Usuario usuario = usuarioDomainService.registrarNovoUsuario(
                command.email(),
                command.senha(),
                command.nome(),
                command.papel());

        // 2. Persiste o agregado
        Usuario salvo = usuarioRepository.save(usuario);

        // 3. Publica evento de domínio para outros contextos
        eventPublisher.publishEvent(new UsuarioCadastradoEvent(
                salvo.id(),
                salvo.email().endereco(),
                salvo.nome(),
                salvo.papeis().iterator().next(), // papel principal
                UsuarioCadastradoEvent.OrigemCadastro.LOCAL));

        return salvo;
    }
}