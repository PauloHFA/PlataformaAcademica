package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.Notificacao;
import java.util.List;

/**
 * Interface do serviço de Notificações.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see NotificacaoServiceImpl
 * @see REQ-005 (Sistema de Notificações)
 */
public interface NotificacaoService {
    void criarNotificacao(UUID usuarioId, String mensagem, String tipo, UUID referenciaId);

    List<Notificacao> listarNotificacoes(UUID usuarioId);

    void marcarComoLida(UUID notificacaoId);

    UUID contarNaoLidas(UUID usuarioId);
}
