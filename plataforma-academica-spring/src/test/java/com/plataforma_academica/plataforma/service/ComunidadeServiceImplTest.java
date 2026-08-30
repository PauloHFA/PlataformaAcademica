package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ComunidadeRepository;
import com.plataforma_academica.plataforma.repository.MembroComunidadeRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunidadeServiceImplTest {

    @Mock
    private ComunidadeRepository comunidadeRepository;

    @Mock
    private MembroComunidadeRepository membroComunidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComunidadeServiceImpl comunidadeService;

    @Test
    void criarComunidade_DeveRetornarComunidade_QuandoValida() {
        // Arrange
        ComunidadeDTO dto = new ComunidadeDTO();
        dto.setDonoId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setNome("Nome");
        dto.setDescricao("Descrição");

        Usuario dono = new Usuario();
        dono.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Comunidade saved = new Comunidade();
        saved.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        saved.setNome("Nome");

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(dono));
        when(comunidadeRepository.save(any(Comunidade.class))).thenReturn(saved);
        when(membroComunidadeRepository.save(any(MembroComunidade.class))).thenReturn(new MembroComunidade());

        // Act
        Comunidade result = comunidadeService.criarComunidade(dto);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        verify(comunidadeRepository).save(any(Comunidade.class));
        verify(membroComunidadeRepository).save(any(MembroComunidade.class));
    }

    @Test
    void criarComunidade_DeveLancarResourceNotFoundException_QuandoDonoNaoEncontrado() {
        // Arrange
        ComunidadeDTO dto = new ComunidadeDTO();
        dto.setDonoId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> comunidadeService.criarComunidade(dto));
        assertEquals("Dono não encontrado", exception.getMessage());
    }

    @Test
    void deletarComunidade_DeveDeletar_QuandoValido() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long solicitanteId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario dono = new Usuario();
        dono.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Comunidade comunidade = new Comunidade();
        comunidade.setId(id);
        comunidade.setDono(dono);

        when(comunidadeRepository.findById(id)).thenReturn(Optional.of(comunidade));

        // Act
        comunidadeService.deletarComunidade(id, solicitanteId);

        // Assert
        verify(comunidadeRepository).delete(comunidade);
    }

    @Test
    void deletarComunidade_DeveLancarResourceNotFoundException_QuandoComunidadeNaoEncontrada() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long solicitanteId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(comunidadeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> comunidadeService.deletarComunidade(id, solicitanteId));
        assertEquals("Comunidade não encontrada", exception.getMessage());
    }

    @Test
    void deletarComunidade_DeveLancarBadRequestException_QuandoNaoEhDono() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long solicitanteId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Usuario dono = new Usuario();
        dono.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Comunidade comunidade = new Comunidade();
        comunidade.setId(id);
        comunidade.setDono(dono);

        when(comunidadeRepository.findById(id)).thenReturn(Optional.of(comunidade));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> comunidadeService.deletarComunidade(id, solicitanteId));
        assertEquals("Apenas o dono pode deletar a comunidade.", exception.getMessage());
    }

    @Test
    void entrarComunidade_DeveRetornarMembro_QuandoValido() {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        MembroComunidade saved = new MembroComunidade();
        saved.setPapel("MEMBRO");

        when(comunidadeRepository.findById(comunidadeId)).thenReturn(Optional.of(comunidade));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)).thenReturn(Optional.empty());
        when(membroComunidadeRepository.save(any(MembroComunidade.class))).thenReturn(saved);

        // Act
        MembroComunidade result = comunidadeService.entrarComunidade(comunidadeId, usuarioId);

        // Assert
        assertNotNull(result);
        assertEquals("MEMBRO", result.getPapel());
        verify(membroComunidadeRepository).save(any(MembroComunidade.class));
    }

    @Test
    void entrarComunidade_DeveLancarBadRequestException_QuandoJaEhMembro() {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        MembroComunidade existente = new MembroComunidade();

        when(comunidadeRepository.findById(comunidadeId)).thenReturn(Optional.of(comunidade));
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)).thenReturn(Optional.of(existente));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> comunidadeService.entrarComunidade(comunidadeId, usuarioId));
        assertEquals("Usuário já é membro", exception.getMessage());
    }

    @Test
    void sairComunidade_DeveDeletar_QuandoEncontrado() {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        MembroComunidade membro = new MembroComunidade();

        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)).thenReturn(Optional.of(membro));

        // Act
        comunidadeService.sairComunidade(comunidadeId, usuarioId);

        // Assert
        verify(membroComunidadeRepository).delete(membro);
    }

    @Test
    void sairComunidade_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> comunidadeService.sairComunidade(comunidadeId, usuarioId));
        assertEquals("Associação não encontrada", exception.getMessage());
    }

    @Test
    void listarTodas_DeveRetornarLista() {
        // Arrange
        List<Comunidade> comunidades = List.of(new Comunidade(), new Comunidade());

        when(comunidadeRepository.findAll()).thenReturn(comunidades);

        // Act
        List<Comunidade> result = comunidadeService.listarTodas();

        // Assert
        assertEquals(comunidades, result);
        verify(comunidadeRepository).findAll();
    }

    @Test
    void listarMembros_DeveRetornarLista() {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<MembroComunidade> membros = List.of(new MembroComunidade());

        when(membroComunidadeRepository.findByComunidadeId(comunidadeId)).thenReturn(membros);

        // Act
        List<MembroComunidade> result = comunidadeService.listarMembros(comunidadeId);

        // Assert
        assertEquals(membros, result);
        verify(membroComunidadeRepository).findByComunidadeId(comunidadeId);
    }
}