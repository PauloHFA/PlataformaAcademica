package com.plataforma_academica.plataforma.identity.domain.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Interface base para todos os eventos de domínio.
 */
public interface DomainEvent {
    UUID eventId();

    Instant occurredOn();

    String eventType();
}