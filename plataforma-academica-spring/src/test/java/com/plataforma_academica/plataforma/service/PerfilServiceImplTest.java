package com.plataforma_academica.plataforma.service;

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
        dto.setUsuarioId(1L);
        dto.setNome("Nome");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Perfil perfil = new Perfil();
        perfil.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findById(1L)).thenReturn(Optional.empty());
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
        dto.setUsuarioId(1L);
        dto.setBio("Bio");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Perfil perfil = new Perfil();
        perfil.setId(1L);
        perfil.setBio("Old Bio");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
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
        dto.setUsuarioId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> perfilService.salvar(dto));
        assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
    }

    @Test
    void atualizar_DeveRetornarPerfil_QuandoEncontrado() {
        // Arrange
        Long id = 1L;
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
        Long id = 1L;
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
        Long id = 1L;

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
        Long id = 1L;

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
        Long usuarioId = 1L;

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
        Long usuarioId = 1L;

        when(perfilRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act
        Perfil result = perfilService.buscarPorUsuarioId(usuarioId);

        // Assert
        assertNull(result);
    }

    @Test
    void existePerfilDoUsuario_DeveRetornarTrue_QuandoExiste() {
        // Arrange
        Long usuarioId = 1L;

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
        Long usuarioId = 1L;

        when(perfilRepository.existsById(usuarioId)).thenReturn(false);

        // Act
        boolean result = perfilService.existePerfilDoUsuario(usuarioId);

        // Assert
        assertFalse(result);
        verify(perfilRepository).existsById(usuarioId);
    }
}