package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.MembroComunidadeRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembroComunidadeServiceImplTest {

    @Mock
    private MembroComunidadeRepository membroComunidadeRepository;

    @InjectMocks
    private MembroComunidadeServiceImpl membroComunidadeService;

    @Test
    void salvar_DeveSalvarMembro_QuandoChamado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(1L);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");

        MembroComunidade salvo = new MembroComunidade();
        salvo.setId(1L);
        salvo.setUsuario(usuario);
        salvo.setComunidade(comunidade);
        salvo.setPapel("MEMBRO");
        salvo.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeRepository.save(any(MembroComunidade.class))).thenReturn(salvo);

        // Act
        MembroComunidade result = membroComunidadeService.salvar(membro);

        // Assert
        assertNotNull(result);
        assertEquals("MEMBRO", result.getPapel());
        verify(membroComunidadeRepository).save(any(MembroComunidade.class));
    }

    @Test
    void buscarPorId_DeveRetornarMembro_QuandoEncontrado() {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(1L);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(id);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeRepository.findById(id)).thenReturn(Optional.of(membro));

        // Act
        MembroComunidade result = membroComunidadeService.buscarPorId(id);

        // Assert
        assertEquals(membro, result);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        when(membroComunidadeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        MembroComunidade result = membroComunidadeService.buscarPorId(id);

        // Assert
        assertNull(result);
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(1L);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro1 = new MembroComunidade();
        membro1.setId(1L);
        membro1.setUsuario(usuario);
        membro1.setComunidade(comunidade);
        membro1.setPapel("MEMBRO");

        MembroComunidade membro2 = new MembroComunidade();
        membro2.setId(2L);
        membro2.setUsuario(usuario);
        membro2.setComunidade(comunidade);
        membro2.setPapel("ADMIN");

        List<MembroComunidade> membros = List.of(membro1, membro2);

        when(membroComunidadeRepository.findAll()).thenReturn(membros);

        // Act
        List<MembroComunidade> result = membroComunidadeService.listarTodos();

        // Assert
        assertEquals(membros, result);
        verify(membroComunidadeRepository).findAll();
    }

    @Test
    void deletar_DeveDeletarPorId() {
        // Arrange
        Long id = 1L;

        // Act
        membroComunidadeService.deletar(id);

        // Assert
        verify(membroComunidadeRepository).deleteById(id);
    }

    @Test
    void buscarPorComunidade_DeveRetornarLista() {
        // Arrange
        Long comunidadeId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(1L);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");

        List<MembroComunidade> membros = List.of(membro);

        when(membroComunidadeRepository.findByComunidadeId(comunidadeId)).thenReturn(membros);

        // Act
        List<MembroComunidade> result = membroComunidadeService.buscarPorComunidade(comunidadeId);

        // Assert
        assertEquals(membros, result);
        verify(membroComunidadeRepository).findByComunidadeId(comunidadeId);
    }

    @Test
    void buscarPorUsuario_DeveRetornarLista() {
        // Arrange
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(1L);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(1L);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");

        List<MembroComunidade> membros = List.of(membro);

        when(membroComunidadeRepository.findByUsuarioId(usuarioId)).thenReturn(membros);

        // Act
        List<MembroComunidade> result = membroComunidadeService.buscarPorUsuario(usuarioId);

        // Assert
        assertEquals(membros, result);
        verify(membroComunidadeRepository).findByUsuarioId(usuarioId);
    }

    @Test
    void buscarPorUsuarioEComunidade_DeveRetornarMembro_QuandoEncontrado() {
        // Arrange
        Long usuarioId = 1L;
        Long comunidadeId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(1L);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId))
                .thenReturn(Optional.of(membro));

        // Act
        MembroComunidade result = membroComunidadeService.buscarPorUsuarioEComunidade(usuarioId, comunidadeId);

        // Assert
        assertEquals(membro, result);
    }

    @Test
    void buscarPorUsuarioEComunidade_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long usuarioId = 1L;
        Long comunidadeId = 1L;

        when(membroComunidadeRepository.findByUsuarioIdAndComunidadeId(usuarioId, comunidadeId))
                .thenReturn(Optional.empty());

        // Act
        MembroComunidade result = membroComunidadeService.buscarPorUsuarioEComunidade(usuarioId, comunidadeId);

        // Assert
        assertNull(result);
    }
}