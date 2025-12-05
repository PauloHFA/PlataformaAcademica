package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ArtigoRepository;
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
class ArtigoServiceImplTest {

    @Mock
    private ArtigoRepository artigoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ArtigoServiceImpl artigoService;

    @Test
    void criar_DeveRetornarArtigo_QuandoValido() {
        // Arrange
        ArtigoDTO dto = new ArtigoDTO();
        dto.setAutorId(1L);
        dto.setTitulo("Título");
        dto.setConteudo("Conteúdo");

        Usuario autor = new Usuario();
        autor.setId(1L);

        Artigo artigoSalvo = new Artigo();
        artigoSalvo.setTitulo("Título");
        artigoSalvo.setConteudo("Conteúdo");
        artigoSalvo.setAutor(autor);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(artigoRepository.save(any(Artigo.class))).thenReturn(artigoSalvo);

        // Act
        Artigo result = artigoService.criar(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Título", result.getTitulo());
        verify(artigoRepository).save(any(Artigo.class));
    }

    @Test
    void criar_DeveLancarResourceNotFoundException_QuandoAutorNaoEncontrado() {
        // Arrange
        ArtigoDTO dto = new ArtigoDTO();
        dto.setAutorId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> artigoService.criar(dto));
        assertEquals("Autor não encontrado", exception.getMessage());
    }

    @Test
    void editar_DeveRetornarArtigo_QuandoValido() {
        // Arrange
        Long id = 1L;
        ArtigoDTO dto = new ArtigoDTO();
        dto.setAutorId(1L);
        dto.setTitulo("Novo Título");
        dto.setConteudo("Novo Conteúdo");

        Usuario autor = new Usuario();
        autor.setId(1L);

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setAutor(autor);

        when(artigoRepository.findById(id)).thenReturn(Optional.of(artigo));
        when(artigoRepository.save(artigo)).thenReturn(artigo);

        // Act
        Artigo result = artigoService.editar(id, dto);

        // Assert
        assertEquals("Novo Título", result.getTitulo());
        verify(artigoRepository).save(artigo);
    }

    @Test
    void editar_DeveLancarResourceNotFoundException_QuandoArtigoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        ArtigoDTO dto = new ArtigoDTO();
        dto.setAutorId(1L);

        when(artigoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> artigoService.editar(id, dto));
        assertEquals("Artigo não encontrado", exception.getMessage());
    }

    @Test
    void editar_DeveLancarBadRequestException_QuandoNaoEhAutor() {
        // Arrange
        Long id = 1L;
        ArtigoDTO dto = new ArtigoDTO();
        dto.setAutorId(2L); // Diferente do autor do artigo

        Usuario autor = new Usuario();
        autor.setId(1L);

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setAutor(autor);

        when(artigoRepository.findById(id)).thenReturn(Optional.of(artigo));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> artigoService.editar(id, dto));
        assertEquals("Apenas o autor pode editar o artigo.", exception.getMessage());
    }

    @Test
    void deletar_DeveDeletar_QuandoValido() {
        // Arrange
        Long id = 1L;
        Long solicitanteId = 1L;

        Usuario autor = new Usuario();
        autor.setId(1L);

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setAutor(autor);

        when(artigoRepository.findById(id)).thenReturn(Optional.of(artigo));

        // Act
        artigoService.deletar(id, solicitanteId);

        // Assert
        verify(artigoRepository).delete(artigo);
    }

    @Test
    void deletar_DeveLancarResourceNotFoundException_QuandoArtigoNaoEncontrado() {
        // Arrange
        Long id = 1L;
        Long solicitanteId = 1L;

        when(artigoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> artigoService.deletar(id, solicitanteId));
        assertEquals("Artigo não encontrado", exception.getMessage());
    }

    @Test
    void deletar_DeveLancarBadRequestException_QuandoNaoEhAutor() {
        // Arrange
        Long id = 1L;
        Long solicitanteId = 2L;

        Usuario autor = new Usuario();
        autor.setId(1L);

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setAutor(autor);

        when(artigoRepository.findById(id)).thenReturn(Optional.of(artigo));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> artigoService.deletar(id, solicitanteId));
        assertEquals("Apenas o autor pode deletar o artigo.", exception.getMessage());
    }

    @Test
    void buscarPorId_DeveRetornarArtigo_QuandoEncontrado() {
        // Arrange
        Long id = 1L;

        Artigo artigo = new Artigo();
        artigo.setId(id);

        when(artigoRepository.findById(id)).thenReturn(Optional.of(artigo));

        // Act
        Artigo result = artigoService.buscarPorId(id);

        // Assert
        assertEquals(artigo, result);
    }

    @Test
    void buscarPorId_DeveLancarResourceNotFoundException_QuandoNaoEncontrado() {
        // Arrange
        Long id = 1L;

        when(artigoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> artigoService.buscarPorId(id));
        assertEquals("Artigo não encontrado", exception.getMessage());
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        // Arrange
        List<Artigo> artigos = List.of(new Artigo(), new Artigo());

        when(artigoRepository.findAll()).thenReturn(artigos);

        // Act
        List<Artigo> result = artigoService.listarTodos();

        // Assert
        assertEquals(artigos, result);
        verify(artigoRepository).findAll();
    }

    @Test
    void listarPorAutor_DeveRetornarLista() {
        // Arrange
        Long autorId = 1L;
        List<Artigo> artigos = List.of(new Artigo());

        when(artigoRepository.findByAutorId(autorId)).thenReturn(artigos);

        // Act
        List<Artigo> result = artigoService.listarPorAutor(autorId);

        // Assert
        assertEquals(artigos, result);
        verify(artigoRepository).findByAutorId(autorId);
    }
}