package com.plataforma_academica.plataforma.identity.domain.event;

import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import com.plataforma_academica.plataforma.identity.domain.model.UsuarioId;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento emitido quando um novo usuário é cadastrado.
 */
public final class UsuarioCadastradoEvent implements DomainEvent {
    private final UUID eventId;
    private final Instant occurredOn;
    private final UsuarioId usuarioId;
    private final String email;
    private final String nome;
    private final Papel papel;
    private final OrigemCadastro origem;

    public UsuarioCadastradoEvent(UsuarioId usuarioId, String email, String nome, Papel papel, OrigemCadastro origem) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.usuarioId = usuarioId;
        this.email = email;
        this.nome = nome;
        this.papel = papel;
        this.origem = origem;
    }

    @Override
    public UUID eventId() {
        return eventId;
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }

    @Override
    public String eventType() {
        return "identity.usuario.cadastrado.v1";
    }

    public UsuarioId usuarioId() {
        return usuarioId;
    }

    public String email() {
        return email;
    }

    public String nome() {
        return nome;
    }

    public Papel papel() {
        return papel;
    }

    public OrigemCadastro origem() {
        return origem;
    }

    public enum OrigemCadastro {
        LOCAL, GOOGLE, FACEBOOK
    }
}