package com.plataforma_academica.plataforma.service;
import java.util.UUID;

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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        String mensagem = "Nova atividade criada";
        String tipo = "ATIVIDADE_CRIADA";
        Long referenciaId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        Notificacao notificacaoSalva = new Notificacao();
        notificacaoSalva.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        String mensagem = "Nova atividade criada";
        String tipo = "ATIVIDADE_CRIADA";
        Long referenciaId = UUID.fromString("00000000-0000-0000-0000-000000000001");

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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        notificacao1.setUsuario(usuario);
        notificacao1.setMensagem("Notificação 1");
        notificacao1.setTipo("ATIVIDADE_CRIADA");
        notificacao1.setReferenciaId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        notificacao1.setLida(false);
        notificacao1.setDataCriacao(LocalDateTime.now());

        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        notificacao2.setUsuario(usuario);
        notificacao2.setMensagem("Notificação 2");
        notificacao2.setTipo("NOTA_ATRIBUIDA");
        notificacao2.setReferenciaId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
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
        Long notificacaoId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
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
        Long notificacaoId = UUID.fromString("00000000-0000-0000-0000-000000000001");

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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long expectedCount = UUID.fromString("00000000-0000-0000-0000-000000000005");

        when(notificacaoRepository.countByUsuarioIdAndLida(usuarioId, false)).thenReturn(expectedCount);

        // Act
        Long result = notificacaoService.contarNaoLidas(usuarioId);

        // Assert
        assertEquals(expectedCount, result);
        verify(notificacaoRepository).countByUsuarioIdAndLida(usuarioId, false);
    }
}