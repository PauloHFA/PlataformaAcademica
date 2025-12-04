package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Notificacao;
import java.util.List;

public interface NotificacaoService {
    void criarNotificacao(Long usuarioId, String mensagem, String tipo, Long referenciaId);
    List<Notificacao> listarNotificacoes(Long usuarioId);
    void marcarComoLida(Long notificacaoId);
    Long contarNaoLidas(Long usuarioId);
}
