package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.model.Postagem;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ComentarioRepository;
import com.plataforma_academica.plataforma.repository.CurtidaRepository;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
import com.plataforma_academica.plataforma.repository.PostagemRepository;
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
class PostagemServiceImplTest {

    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PlataformaRepository plataformaRepository;

    @Mock
    private AmizadeService amizadeService;

    @Mock
    private CurtidaRepository curtidaRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private PostagemServiceImpl postagemService;

    @Test
    void publicar_DeveRetornarPostagemDTO_QuandoValido() {
        // Arrange
        PostagemDTO dto = new PostagemDTO();
        dto.setAutorId(1L);
        dto.setPlataformaId(1L);
        dto.setTitulo("Título");

        Usuario autor = new Usuario();
        autor.setId(1L);

        Plataforma plataforma = new Plataforma();
        plataforma.setId(1L);

        Postagem postagem = new Postagem();
        postagem.setId(2L);
        postagem.setTitulo("Título");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(plataformaRepository.findById(1L)).thenReturn(Optional.of(plataforma));
        when(postagemRepository.save(any(Postagem.class))).thenReturn(postagem);

        // Act
        PostagemDTO result = postagemService.publicar(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Título", result.getTitulo());
        verify(postagemRepository).save(any(Postagem.class));
    }

    @Test
    void publicar_DeveLancarRuntimeException_QuandoAutorNaoEncontrado() {
        // Arrange
        PostagemDTO dto = new PostagemDTO();
        dto.setAutorId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postagemService.publicar(dto));
        assertEquals("Autor não encontrado", exception.getMessage());
    }

    @Test
    void publicar_DeveLancarRuntimeException_QuandoPlataformaNaoEncontrada() {
        // Arrange
        PostagemDTO dto = new PostagemDTO();
        dto.setAutorId(1L);
        dto.setPlataformaId(1L);

        Usuario autor = new Usuario();
        autor.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(plataformaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postagemService.publicar(dto));
        assertEquals("Plataforma não encontrada", exception.getMessage());
    }

    @Test
    void deletar_DeveDeletar_QuandoEncontrada() {
        // Arrange
        Long id = 1L;

        Postagem postagem = new Postagem();
        postagem.setId(id);

        when(postagemRepository.findById(id)).thenReturn(Optional.of(postagem));

        // Act
        postagemService.deletar(id);

        // Assert
        verify(comentarioRepository).deleteByPostagemId(id);
        verify(curtidaRepository).deleteByPostagemId(id);
        verify(postagemRepository).deleteById(id);
    }

    @Test
    void deletar_DeveLancarRuntimeException_QuandoNaoEncontrada() {
        // Arrange
        Long id = 1L;

        when(postagemRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postagemService.deletar(id));
        assertEquals("Postagem não encontrada", exception.getMessage());
    }

    @Test
    void buscarPorId_DeveRetornarPostagemDTO_QuandoEncontrada() {
        // Arrange
        Long id = 1L;

        Postagem postagem = new Postagem();
        postagem.setId(id);

        when(postagemRepository.findById(id)).thenReturn(Optional.of(postagem));

        // Act
        PostagemDTO result = postagemService.buscarPorId(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrada() {
        // Arrange
        Long id = 1L;

        when(postagemRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        PostagemDTO result = postagemService.buscarPorId(id);

        // Assert
        assertNull(result);
    }

    @Test
    void listarTodas_DeveRetornarLista() {
        // Arrange
        List<Postagem> postagens = List.of(new Postagem(), new Postagem());

        when(postagemRepository.findAll()).thenReturn(postagens);

        // Act
        List<PostagemDTO> result = postagemService.listarTodas();

        // Assert
        assertEquals(2, result.size());
        verify(postagemRepository).findAll();
    }
}