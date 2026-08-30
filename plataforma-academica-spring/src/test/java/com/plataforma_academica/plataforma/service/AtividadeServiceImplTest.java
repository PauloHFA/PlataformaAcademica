package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.Professor;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtividadeServiceImplTest {

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SaladeAulaRepository salaDeAulaRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private AtividadeServiceImpl atividadeService;

    @Test
    void criarAtividade_ComAtividadeObjeto_DeveRetornarAtividade_QuandoValido() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setUsuarios(List.of()); // Empty list for simplicity

        Professor autor = new Professor();
        autor.setId(autorId);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Título");

        Atividade saved = new Atividade();
        saved.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        saved.setTitulo("Título");

        when(salaDeAulaRepository.findById(salaId)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(atividadeRepository.save(atividade)).thenReturn(saved);

        // Act
        Atividade result = atividadeService.criarAtividade(salaId, atividade, autorId);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        verify(atividadeRepository).save(atividade);
    }

    @Test
    void criarAtividade_ComAtividadeObjeto_DeveLancarRuntimeException_QuandoSalaNaoEncontrada() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Atividade atividade = new Atividade();

        when(salaDeAulaRepository.findById(salaId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> atividadeService.criarAtividade(salaId, atividade, autorId));
        assertEquals("Sala não encontrada", exception.getMessage());
    }

    @Test
    void criarAtividade_ComAtividadeObjeto_DeveLancarRuntimeException_QuandoAutorNaoEncontrado() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Atividade atividade = new Atividade();

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);

        when(salaDeAulaRepository.findById(salaId)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(autorId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> atividadeService.criarAtividade(salaId, atividade, autorId));
        assertEquals("Autor não encontrado", exception.getMessage());
    }

    @Test
    void criarAtividade_ComAtividadeObjeto_DeveLancarSecurityException_QuandoNaoEhProfessor() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Atividade atividade = new Atividade();

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);

        Usuario autor = new Usuario(); // Not Professor
        autor.setId(autorId);

        when(salaDeAulaRepository.findById(salaId)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(autorId)).thenReturn(Optional.of(autor));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> atividadeService.criarAtividade(salaId, atividade, autorId));
        assertEquals("Apenas professores podem criar atividades.", exception.getMessage());
    }

    @Test
    void criarAtividade_ComAtividadeDTO_DeveRetornarAtividade_QuandoValido() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        AtividadeDTO dto = new AtividadeDTO();
        dto.setTitulo("Título");
        dto.setDescricao("Descrição");
        dto.setDataEntrega("2023-12-31");

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setUsuarios(List.of());

        Professor autor = new Professor();
        autor.setId(autorId);

        Atividade saved = new Atividade();
        saved.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        when(salaDeAulaRepository.findById(salaId)).thenReturn(Optional.of(sala));
        when(usuarioRepository.findById(autorId)).thenReturn(Optional.of(autor));
        when(atividadeRepository.save(any(Atividade.class))).thenReturn(saved);

        // Act
        Atividade result = atividadeService.criarAtividade(salaId, dto, autorId);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        verify(atividadeRepository).save(any(Atividade.class));
    }

    @Test
    void buscarAtividadePorId_DeveRetornarAtividade_QuandoEncontrada() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(atividade));

        // Act
        Atividade result = atividadeService.buscarAtividadePorId(atividadeId);

        // Assert
        assertEquals(atividade, result);
    }

    @Test
    void buscarAtividadePorId_DeveLancarRuntimeException_QuandoNaoEncontrada() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> atividadeService.buscarAtividadePorId(atividadeId));
        assertEquals("Atividade não encontrada", exception.getMessage());
    }

    @Test
    void listarAtividadesPorSala_DeveRetornarLista() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<Atividade> atividades = List.of(new Atividade());

        when(atividadeRepository.findBySalaDeAulaId(salaId)).thenReturn(atividades);

        // Act
        List<Atividade> result = atividadeService.listarAtividadesPorSala(salaId);

        // Assert
        assertEquals(atividades, result);
        verify(atividadeRepository).findBySalaDeAulaId(salaId);
    }

    @Test
    void atualizarAtividade_ComAtividadeObjeto_DeveRetornarAtividade_QuandoValido() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Atividade existente = new Atividade();
        existente.setId(atividadeId);
        Usuario autor = new Usuario();
        autor.setId(autorId);
        existente.setAutor(autor);

        Atividade atualizada = new Atividade();
        atualizada.setTitulo("Novo Título");

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(existente));
        when(atividadeRepository.save(existente)).thenReturn(existente);

        // Act
        Atividade result = atividadeService.atualizarAtividade(atividadeId, atualizada, autorId);

        // Assert
        assertEquals("Novo Título", result.getTitulo());
        verify(atividadeRepository).save(existente);
    }

    @Test
    void atualizarAtividade_ComAtividadeObjeto_DeveLancarRuntimeException_QuandoNaoEhAutor() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Atividade existente = new Atividade();
        existente.setId(atividadeId);
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        existente.setAutor(autor);

        Atividade atualizada = new Atividade();

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(existente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> atividadeService.atualizarAtividade(atividadeId, atualizada, autorId));
        assertEquals("Você não tem permissão para atualizar esta atividade", exception.getMessage());
    }

    @Test
    void deletarAtividade_DeveDeletar_QuandoValido() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Atividade existente = new Atividade();
        existente.setId(atividadeId);
        Usuario autor = new Usuario();
        autor.setId(autorId);
        existente.setAutor(autor);

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(existente));

        // Act
        atividadeService.deletarAtividade(atividadeId, autorId);

        // Assert
        verify(atividadeRepository).delete(existente);
    }

    @Test
    void deletarAtividade_DeveLancarRuntimeException_QuandoNaoEhAutor() {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Atividade existente = new Atividade();
        existente.setId(atividadeId);
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        existente.setAutor(autor);

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(existente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> atividadeService.deletarAtividade(atividadeId, autorId));
        assertEquals("Você não tem permissão para deletar esta atividade", exception.getMessage());
    }

    @Test
    void listarAtividadesPorAutor_DeveRetornarLista() {
        // Arrange
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<Atividade> atividades = List.of(new Atividade());

        when(atividadeRepository.findByAutorId(autorId)).thenReturn(atividades);

        // Act
        List<Atividade> result = atividadeService.listarAtividadesPorAutor(autorId);

        // Assert
        assertEquals(atividades, result);
        verify(atividadeRepository).findByAutorId(autorId);
    }
}