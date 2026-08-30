package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Comentario;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.TipoDestinoComentario;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ComentarioRepository;
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
class ComentarioServiceImplTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private ComentarioServiceImpl comentarioService;

    @Test
    void salvar_DeveRetornarComentario_QuandoValido() {
        // Arrange
        Comentario comentario = new Comentario();
        comentario.setConteudo("Conteúdo");

        Comentario saved = new Comentario();
        saved.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        saved.setConteudo("Conteúdo");

        when(comentarioRepository.save(comentario)).thenReturn(saved);

        // Act
        Comentario result = comentarioService.salvar(comentario);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        verify(comentarioRepository).save(comentario);
    }

    @Test
    void buscarPorId_DeveRetornarComentario_QuandoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Comentario comentario = new Comentario();
        comentario.setId(id);

        when(comentarioRepository.findById(id)).thenReturn(Optional.of(comentario));

        // Act
        Comentario result = comentarioService.buscarPorId(id);

        // Assert
        assertEquals(comentario, result);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(comentarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Comentario result = comentarioService.buscarPorId(id);

        // Assert
        assertNull(result);
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        // Arrange
        List<Comentario> comentarios = List.of(new Comentario(), new Comentario());

        when(comentarioRepository.findAll()).thenReturn(comentarios);

        // Act
        List<Comentario> result = comentarioService.listarTodos();

        // Assert
        assertEquals(comentarios, result);
        verify(comentarioRepository).findAll();
    }

    @Test
    void atualizar_DeveRetornarComentario_QuandoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Comentario existente = new Comentario();
        existente.setId(id);
        existente.setConteudo("Old");

        Comentario atualizado = new Comentario();
        atualizado.setConteudo("New");

        when(comentarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(comentarioRepository.save(existente)).thenReturn(existente);

        // Act
        Comentario result = comentarioService.atualizar(id, atualizado);

        // Assert
        assertEquals("New", result.getConteudo());
        verify(comentarioRepository).save(existente);
    }

    @Test
    void atualizar_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Comentario atualizado = new Comentario();

        when(comentarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Comentario result = comentarioService.atualizar(id, atualizado);

        // Assert
        assertNull(result);
    }

    @Test
    void deletar_DeveDeletar() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Act
        comentarioService.deletar(id);

        // Assert
        verify(comentarioRepository).deleteById(id);
    }

    @Test
    void listarComentariosPorSala_DeveRetornarLista() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<Comentario> comentarios = List.of(new Comentario());

        when(comentarioRepository.findBySaladeAulaId(salaId)).thenReturn(comentarios);

        // Act
        List<Comentario> result = comentarioService.listarComentariosPorSala(salaId);

        // Assert
        assertEquals(comentarios, result);
        verify(comentarioRepository).findBySaladeAulaId(salaId);
    }

    @Test
    void listarComentariosPorAtividade_DeveRetornarLista() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<Comentario> comentarios = List.of(new Comentario());

        when(comentarioRepository.findByAtividadeId(atividadeId)).thenReturn(comentarios);

        // Act
        List<Comentario> result = comentarioService.listarComentariosPorAtividade(atividadeId);

        // Assert
        assertEquals(comentarios, result);
        verify(comentarioRepository).findByAtividadeId(atividadeId);
    }

    @Test
    void listarComentariosPorPostagem_DeveRetornarLista() {
        // Arrange
        Long postagemId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<Comentario> comentarios = List.of(new Comentario());

        when(comentarioRepository.findByPostagemId(postagemId)).thenReturn(comentarios);

        // Act
        List<Comentario> result = comentarioService.listarComentariosPorPostagem(postagemId);

        // Assert
        assertEquals(comentarios, result);
        verify(comentarioRepository).findByPostagemId(postagemId);
    }

    @Test
    void salvarComentarioSala_DeveRetornarComentario_QuandoValido() {
        // Arrange
        Comentario comentario = new Comentario();
        comentario.setConteudo("Conteúdo");

        SaladeAula sala = new SaladeAula();
        sala.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setSaladeAula(sala);

        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setAutor(autor);

        Comentario saved = new Comentario();
        saved.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        when(comentarioRepository.save(any(Comentario.class))).thenReturn(saved);

        // Act
        Comentario result = comentarioService.salvarComentarioSala(comentario);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        assertEquals(TipoDestinoComentario.SALADEAULA, comentario.getTipoDestino());
        verify(comentarioRepository).save(any(Comentario.class));
    }

    @Test
    void salvarComentarioSala_DeveLancarIllegalArgumentException_QuandoSalaNula() {
        // Arrange
        Comentario comentario = new Comentario();
        comentario.setConteudo("Conteúdo");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> comentarioService.salvarComentarioSala(comentario));
        assertEquals("Sala de aula é obrigatória", exception.getMessage());
    }

    @Test
    void salvarComentarioSala_DeveLancarIllegalArgumentException_QuandoAutorNulo() {
        // Arrange
        Comentario comentario = new Comentario();
        comentario.setConteudo("Conteúdo");

        SaladeAula sala = new SaladeAula();
        sala.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setSaladeAula(sala);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> comentarioService.salvarComentarioSala(comentario));
        assertEquals("Autor é obrigatório", exception.getMessage());
    }
}