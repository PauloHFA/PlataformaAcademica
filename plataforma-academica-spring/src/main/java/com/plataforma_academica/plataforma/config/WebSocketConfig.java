package com.plataforma_academica.plataforma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * ==========================================================================================================
 * WEBSOCKET CONFIG — Plataforma Acadêmica
 * ==========================================================================================================
 * Configuração de comunicação em tempo real via WebSocket e STOMP.
 * 
 * Camada: Infrastructure / Realtime Configuration
 * Responsabilidades: Habilitar message broker para chat e notificações em tempo
 * real.
 * 
 * @see REQ-030 (Chat e Notificações em Tempo Real)
 *      ==========================================================================================================
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}