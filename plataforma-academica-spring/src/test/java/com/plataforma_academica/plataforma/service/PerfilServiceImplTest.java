package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.PerfilRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceImplTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PerfilServiceImpl perfilService;

    @Test
    void salvar_DeveCriarNovoPerfil_QuandoNaoExiste() {
        // Arrange
        PerfilDTO dto = new PerfilDTO();
        dto.setUsuarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setNome("Nome");

        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Perfil perfil = new Perfil();
        perfil.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(usuario));
        when(perfilRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // Act
        Perfil result = perfilService.salvar(dto);

        // Assert
        assertNotNull(result);
        assertEquals(perfil, result);
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    void salvar_DeveAtualizarPerfil_QuandoExiste() {
        // Arrange
        PerfilDTO dto = new PerfilDTO();
        dto.setUsuarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setBio("Bio");

        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Perfil perfil = new Perfil();
        perfil.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        perfil.setBio("Old Bio");

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(usuario));
        when(perfilRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(perfil)).thenReturn(perfil);

        // Act
        Perfil result = perfilService.salvar(dto);

        // Assert
        assertEquals("Bio", result.getBio());
        verify(perfilRepository).save(perfil);
    }

    @Test
    void salvar_DeveLancarRuntimeException_QuandoUsuarioNaoEncontrado() {
        // Arrange
        PerfilDTO dto = new PerfilDTO();
        dto.setUsuarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> perfilService.salvar(dto));
        assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void atualizar_DeveRetornarPerfil_QuandoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        PerfilDTO dto = new PerfilDTO();
        dto.setBio("Nova Bio");

        Perfil perfil = new Perfil();
        perfil.setId(id);

        when(perfilRepository.findById(id)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(perfil)).thenReturn(perfil);

        // Act
        Perfil result = perfilService.atualizar(id, dto);

        // Assert
        assertEquals("Nova Bio", result.getBio());
        verify(perfilRepository).save(perfil);
    }

    @Test
    void atualizar_DeveLancarRuntimeException_QuandoNaoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        PerfilDTO dto = new PerfilDTO();

        when(perfilRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> perfilService.atualizar(id, dto));
        assertEquals("Perfil não encontrado", exception.getMessage());
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        // Arrange
        List<Perfil> perfis = List.of(new Perfil(), new Perfil());

        when(perfilRepository.findAll()).thenReturn(perfis);

        // Act
        List<Perfil> result = perfilService.listarTodos();

        // Assert
        assertEquals(perfis, result);
        verify(perfilRepository).findAll();
    }

    @Test
    void buscarPorId_DeveRetornarPerfil_QuandoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Perfil perfil = new Perfil();
        perfil.setId(id);

        when(perfilRepository.findById(id)).thenReturn(Optional.of(perfil));

        // Act
        Perfil result = perfilService.buscarPorId(id);

        // Assert
        assertEquals(perfil, result);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(perfilRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Perfil result = perfilService.buscarPorId(id);

        // Assert
        assertNull(result);
    }

    @Test
    void buscarPorCurso_DeveRetornarLista() {
        // Arrange
        String curso = "Engenharia";
        List<Perfil> perfis = List.of(new Perfil());

        when(perfilRepository.findByCurso(curso)).thenReturn(perfis);

        // Act
        List<Perfil> result = perfilService.buscarPorCurso(curso);

        // Assert
        assertEquals(perfis, result);
        verify(perfilRepository).findByCurso(curso);
    }

    @Test
    void buscarPorUsuarioId_DeveRetornarPerfil_QuandoEncontrado() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Perfil perfil = new Perfil();
        perfil.setId(usuarioId);

        when(perfilRepository.findById(usuarioId)).thenReturn(Optional.of(perfil));

        // Act
        Perfil result = perfilService.buscarPorUsuarioId(usuarioId);

        // Assert
        assertEquals(perfil, result);
    }

    @Test
    void buscarPorUsuarioId_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(perfilRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act
        Perfil result = perfilService.buscarPorUsuarioId(usuarioId);

        // Assert
        assertNull(result);
    }

    @Test
    void existePerfilDoUsuario_DeveRetornarTrue_QuandoExiste() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(perfilRepository.existsById(usuarioId)).thenReturn(true);

        // Act
        boolean result = perfilService.existePerfilDoUsuario(usuarioId);

        // Assert
        assertTrue(result);
        verify(perfilRepository).existsById(usuarioId);
    }

    @Test
    void existePerfilDoUsuario_DeveRetornarFalse_QuandoNaoExiste() {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(perfilRepository.existsById(usuarioId)).thenReturn(false);

        // Act
        boolean result = perfilService.existePerfilDoUsuario(usuarioId);

        // Assert
        assertFalse(result);
        verify(perfilRepository).existsById(usuarioId);
    }
}