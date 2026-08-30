package com.plataforma_academica.plataforma.service;
import java.util.UUID;

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
        dto.setSolicitanteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        Usuario solicitante = new Usuario();
        solicitante.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Usuario destinatario = new Usuario();
        destinatario.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        Amizade amizadeSalva = new Amizade();
        amizadeSalva.setSolicitante(solicitante);
        amizadeSalva.setDestinatario(destinatario);
        amizadeSalva.setStatus(Amizade.Status.PENDENTE);

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000002"))).thenReturn(Optional.of(destinatario));
        when(amizadeRepository.findBySolicitanteIdAndDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"))).thenReturn(Optional.empty());
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
        dto.setSolicitanteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Não é possível enviar solicitação para si mesmo.", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarResourceNotFoundException_QuandoSolicitanteNaoEncontrado() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Solicitante não encontrado", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarResourceNotFoundException_QuandoDestinatarioNaoEncontrado() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        Usuario solicitante = new Usuario();
        solicitante.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000002"))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Destinatário não encontrado", exception.getMessage());
    }

    @Test
    void enviarSolicitacao_DeveLancarBadRequestException_QuandoSolicitacaoJaExiste() {
        // Arrange
        AmizadeDTO dto = new AmizadeDTO();
        dto.setSolicitanteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        Usuario solicitante = new Usuario();
        solicitante.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Usuario destinatario = new Usuario();
        destinatario.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        Amizade amizadeExistente = new Amizade();

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(solicitante));
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000002"))).thenReturn(Optional.of(destinatario));
        when(amizadeRepository.findBySolicitanteIdAndDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"))).thenReturn(Optional.of(amizadeExistente));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> amizadeService.enviarSolicitacao(dto));
        assertEquals("Solicitação já existe.", exception.getMessage());
    }

    @Test
    void responderSolicitacao_DeveAceitar_QuandoAcaoAceitar() {
        // Arrange
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
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
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
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
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
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
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
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
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        String acao = "aceitar";

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.responderSolicitacao(amizadeId, acao));
        assertEquals("Solicitação não encontrada", exception.getMessage());
    }

    @Test
    void removerAmizade_DeveDeletar_QuandoEncontrada() {
        // Arrange
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

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
        Long amizadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(amizadeRepository.findById(amizadeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.removerAmizade(amizadeId));
        assertEquals("Amizade não encontrada", exception.getMessage());
    }

    @Test
    void listarSolicitacoesPendentes_DeveRetornarLista_QuandoUsuarioEncontrado() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.listarSolicitacoesPendentes(usuarioId));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void listarAmigos_DeveRetornarLista_QuandoUsuarioEncontrado() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

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
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> amizadeService.listarAmigos(usuarioId));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}