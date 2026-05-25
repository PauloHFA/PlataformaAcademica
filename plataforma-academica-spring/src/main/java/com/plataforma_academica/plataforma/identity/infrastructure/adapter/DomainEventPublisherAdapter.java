package com.plataforma_academica.plataforma.identity.infrastructure.adapter;

import com.plataforma_academica.plataforma.identity.domain.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisherAdapter {

    private final ApplicationEventPublisher eventPublisher;

    public DomainEventPublisherAdapter(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publish(DomainEvent event) {
        eventPublisher.publishEvent(event);
    }
}
