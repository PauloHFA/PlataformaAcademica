package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Notificacao;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.NotificacaoRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceImplTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private NotificacaoServiceImpl notificacaoService;

    @Test
    void criarNotificacao_DeveSalvarNotificacao_QuandoUsuarioEncontrado() {
        // Arrange
        Long usuarioId = 1L;
        String mensagem = "Nova atividade criada";
        String tipo = "ATIVIDADE_CRIADA";
        Long referenciaId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Notificacao notificacaoSalva = new Notificacao();
        notificacaoSalva.setId(1L);
        notificacaoSalva.setUsuario(usuario);
        notificacaoSalva.setMensagem(mensagem);
        notificacaoSalva.setTipo(tipo);
        notificacaoSalva.setReferenciaId(referenciaId);
        notificacaoSalva.setLida(false);
        notificacaoSalva.setDataCriacao(LocalDateTime.now());

        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacaoSalva);

        // Act
        notificacaoService.criarNotificacao(usuarioId, mensagem, tipo, referenciaId);

        // Assert
        verify(usuarioRepository).findById(usuarioId);
        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    void criarNotificacao_DeveLancarException_QuandoUsuarioNaoEncontrado() {
        // Arrange
        Long usuarioId = 1L;
        String mensagem = "Nova atividade criada";
        String tipo = "ATIVIDADE_CRIADA";
        Long referenciaId = 1L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            notificacaoService.criarNotificacao(usuarioId, mensagem, tipo, referenciaId));

        verify(usuarioRepository).findById(usuarioId);
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }

    @Test
    void listarNotificacoes_DeveRetornarLista() {
        // Arrange
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        notificacao1.setUsuario(usuario);
        notificacao1.setMensagem("Notificação 1");
        notificacao1.setTipo("ATIVIDADE_CRIADA");
        notificacao1.setReferenciaId(1L);
        notificacao1.setLida(false);
        notificacao1.setDataCriacao(LocalDateTime.now());

        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(2L);
        notificacao2.setUsuario(usuario);
        notificacao2.setMensagem("Notificação 2");
        notificacao2.setTipo("NOTA_ATRIBUIDA");
        notificacao2.setReferenciaId(2L);
        notificacao2.setLida(true);
        notificacao2.setDataCriacao(LocalDateTime.now().minusHours(1));

        List<Notificacao> notificacoes = List.of(notificacao1, notificacao2);

        when(notificacaoRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId)).thenReturn(notificacoes);

        // Act
        List<Notificacao> result = notificacaoService.listarNotificacoes(usuarioId);

        // Assert
        assertEquals(notificacoes, result);
        verify(notificacaoRepository).findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    @Test
    void marcarComoLida_DeveMarcarComoLida_QuandoNotificacaoEncontrada() {
        // Arrange
        Long notificacaoId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        Notificacao notificacao = new Notificacao();
        notificacao.setId(notificacaoId);
        notificacao.setUsuario(usuario);
        notificacao.setMensagem("Notificação");
        notificacao.setTipo("ATIVIDADE_CRIADA");
        notificacao.setLida(false);
        notificacao.setDataCriacao(LocalDateTime.now());

        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.of(notificacao));

        // Act
        notificacaoService.marcarComoLida(notificacaoId);

        // Assert
        assertTrue(notificacao.getLida());
        verify(notificacaoRepository).findById(notificacaoId);
        verify(notificacaoRepository).save(notificacao);
    }

    @Test
    void marcarComoLida_DeveLancarException_QuandoNotificacaoNaoEncontrada() {
        // Arrange
        Long notificacaoId = 1L;

        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            notificacaoService.marcarComoLida(notificacaoId));

        verify(notificacaoRepository).findById(notificacaoId);
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }

    @Test
    void contarNaoLidas_DeveRetornarContagem() {
        // Arrange
        Long usuarioId = 1L;
        Long expectedCount = 5L;

        when(notificacaoRepository.countByUsuarioIdAndLida(usuarioId, false)).thenReturn(expectedCount);

        // Act
        Long result = notificacaoService.contarNaoLidas(usuarioId);

        // Assert
        assertEquals(expectedCount, result);
        verify(notificacaoRepository).countByUsuarioIdAndLida(usuarioId, false);
    }
}