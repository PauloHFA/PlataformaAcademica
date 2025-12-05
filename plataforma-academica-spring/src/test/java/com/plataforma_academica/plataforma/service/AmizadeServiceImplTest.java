package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AmizadeRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmizadeServiceImplTest {

    @Mock
    private AmizadeRepository amizadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AmizadeServiceImpl amizadeService;

    @Test
    void enviarSolicitacao_DeveRetornarAmizade_QuandoValida() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(1L);
        dto.setDestinatarioId(2L);

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);

        Amizade amizadeSalva = new Amizade();
        amizadeSalva.setSolicitante(solicitante);
        amizadeSalva.setDestinatario(destinatario);
        amizadeSalva.setStatus(Amizade.Status.PENDENTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(destinatario));
        when(amizadeRepository.findBySolicitanteIdAndDestinatarioId(1L, 2L)).thenReturn(Optional.empty());
        when(amizadeRepository.save(any(Amizade.class))).thenReturn(amizadeSalva);

        // Act
        Amizade result = amizadeService.enviarSolicitacao(dto);

        // Assert
        assertNotNull(result);
        assertEquals(Amizade.Status.PENDENTE, result.getStatus());
        verify(amizadeRepository).save(any(Amizade.class));
    }

    @Test
    void enviarSolicitacao_DeveLancarBadRequestException_QuandoSolicitanteIgualDestinatario() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(1L);
        dto.setDestinatarioId(1L);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Não é possível enviar solicitação para si mesmo.", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarResourceNotFoundException_QuandoSolicitanteNaoEncontrado() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(1L);
        dto.setDestinatarioId(2L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Solicitante não encontrado", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarResourceNotFoundException_QuandoDestinatarioNaoEncontrado() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(1L);
        dto.setDestinatarioId(2L);

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Destinatário não encontrado", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarBadRequestException_QuandoSolicitacaoJaExiste() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(1L);
        dto.setDestinatarioId(2L);

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);

        Amizade amizadeExistente = new Amizade();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(destinatario));
        when(amizadeRepository.findBySolicitanteIdAndDestinatarioId(1L, 2L)).thenReturn(Optional.of(amizadeExistente));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Solicitação já existe.", exception.getMessage());
    }

    @Test
    void responderSolicitacao_DeveAceitar_QuandoAcaoAceitar() {
        // Arrange
        Long amizadeId = 1L;
        String acao = "aceitar";

        Amizade amizade = new Amizade();
        amizade.setId(amizadeId);
        amizade.setStatus(Amizade.Status.PENDENTE);

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.of(amizade));
        when(amizadeRepository.save(amizade)).thenReturn(amizade);

        // Act
        Amizade result = amizadeService.responderSolicitacao(amizadeId, acao);

        // Assert
        assertEquals(Amizade.Status.ACEITO, result.getStatus());
        verify(amizadeRepository).save(amizade);
    }

    @Test
    void responderSolicitacao_DeveRecusar_QuandoAcaoRecusar() {
        // Arrange
        Long amizadeId = 1L;
        String acao = "recusar";

        Amizade amizade = new Amizade();
        amizade.setId(amizadeId);
        amizade.setStatus(Amizade.Status.PENDENTE);

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.of(amizade));
        when(amizadeRepository.save(amizade)).thenReturn(amizade);

        // Act
        Amizade result = amizadeService.responderSolicitacao(amizadeId, acao);

        // Assert
        assertEquals(Amizade.Status.RECUSADO, result.getStatus());
        verify(amizadeRepository).save(amizade);
    }

    @Test
    void responderSolicitacao_DeveLancarBadRequestException_QuandoAcaoInvalida() {
        // Arrange
        Long amizadeId = 1L;
        String acao = "invalida";

        Amizade amizade = new Amizade();
        amizade.setId(amizadeId);
        amizade.setStatus(Amizade.Status.PENDENTE);

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.of(amizade));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.responderSolicitacao(amizadeId, acao));
        assertEquals("Ação inválida. Use 'aceitar' ou 'recusar'.", exception.getMessage());
    }

    @Test
    void responderSolicitacao_DeveLancarBadRequestException_QuandoJaRespondida() {
        // Arrange
        Long amizadeId = 1L;
        String acao = "aceitar";

        Amizade amizade = new Amizade();
        amizade.setId(amizadeId);
        amizade.setStatus(Amizade.Status.ACEITO);

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.of(amizade));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.responderSolicitacao(amizadeId, acao));
        assertEquals("Solicitação já foi respondida.", exception.getMessage());
    }

    @Test
    void responderSolicitacao_DeveLancarResourceNotFoundException_QuandoNaoEncontrada() {
        // Arrange
        Long amizadeId = 1L;
        String acao = "aceitar";

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.responderSolicitacao(amizadeId, acao));
        assertEquals("Solicitação não encontrada", exception.getMessage());
    }

    @Test
    void removerAmizade_DeveDeletar_QuandoEncontrada() {
        // Arrange
        Long amizadeId = 1L;

        Amizade amizade = new Amizade();
        amizade.setId(amizadeId);

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.of(amizade));

        // Act
        amizadeService.removerAmizade(amizadeId);

        // Assert
        verify(amizadeRepository).delete(amizade);
    }

    @Test
    void removerAmizade_DeveLancarResourceNotFoundException_QuandoNaoEncontrada() {
        // Arrange
        Long amizadeId = 1L;

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.removerAmizade(amizadeId));
        assertEquals("Amizade não encontrada", exception.getMessage());
    }

    @Test
    void listarSolicitacoesPendentes_DeveRetornarLista_QuandoUsuarioEncontrado() {
        // Arrange
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        List<Amizade> solicitacoes = List.of(new Amizade());

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(amizadeRepository.findSolicitacoesPendentes(usuarioId)).thenReturn(solicitacoes);

        // Act
        List<Amizade> result = amizadeService.listarSolicitacoesPendentes(usuarioId);

        // Assert
        assertEquals(solicitacoes, result);
        verify(amizadeRepository).findSolicitacoesPendentes(usuarioId);
    }

    @Test
    void listarSolicitacoesPendentes_DeveLancarResourceNotFoundException_QuandoUsuarioNaoEncontrado() {
        // Arrange
        Long usuarioId = 1L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.listarSolicitacoesPendentes(usuarioId));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void listarAmigos_DeveRetornarLista_QuandoUsuarioEncontrado() {
        // Arrange
        Long usuarioId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        List<Amizade> amigos = List.of(new Amizade());

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(amizadeRepository.findAmigosAceitos(usuarioId)).thenReturn(amigos);

        // Act
        List<Amizade> result = amizadeService.listarAmigos(usuarioId);

        // Assert
        assertEquals(amigos, result);
        verify(amizadeRepository).findAmigosAceitos(usuarioId);
    }

    @Test
    void listarAmigos_DeveLancarResourceNotFoundException_QuandoUsuarioNaoEncontrado() {
        // Arrange
        Long usuarioId = 1L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.listarAmigos(usuarioId));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}