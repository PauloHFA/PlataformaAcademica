package com.plataforma_academica.plataforma.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarNotificacao(String destino, String mensagem) {
        messagingTemplate.convertAndSend(destino, mensagem);
    }

    public void notificarUsuario(Long usuarioId, String mensagem) {
        enviarNotificacao("/topic/notificacoes/" + usuarioId, mensagem);
    }
}