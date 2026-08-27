package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Notificacao;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.NotificacaoRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementação do serviço de notificações.
 * 
 * Camada: Application Service
 * Responsabilidades: Criar, listar e gerenciar notificações para usuários,
 * vinculando mensagens a eventos de referência no sistema.
 */
/**
 * Implementação do serviço de Notificações.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Padrões aplicados: Service Layer, Repository Pattern.
 * 
 * @see NotificacaoService
 * @see REQ-005 (Sistema de Notificações)
 */
@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void criarNotificacao(Long usuarioId, String mensagem, String tipo, Long referenciaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        notificacao.setReferenciaId(referenciaId);
        notificacaoRepository.save(notificacao);

        // Enviar via WebSocket
        notificationService.notificarUsuario(usuarioId, mensagem);
    }

    @Override
    public List<Notificacao> listarNotificacoes(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    @Override
    public void marcarComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId).orElseThrow();
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    @Override
    public Long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository.countByUsuarioIdAndLida(usuarioId, false);
    }
}
