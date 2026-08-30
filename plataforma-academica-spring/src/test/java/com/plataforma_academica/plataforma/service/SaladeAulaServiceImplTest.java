package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.*;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaladeAulaServiceImplTest {

    @Mock
    private SaladeAulaRepository salaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private SaladeAulaServiceImpl salaService;

    @Test
    void buscarSalaPorId_DeveRetornarSala_QuandoEncontrada() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setNome("Sala Teste");

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(sala));

        // Act
        SaladeAula result = salaService.buscarSalaPorId(salaId);

        // Assert
        assertEquals(sala, result);
        verify(salaRepository).findById(salaId);
    }

    @Test
    void buscarSalaPorId_DeveLancarException_QuandoNaoEncontrada() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(salaRepository.findById(salaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () ->
            salaService.buscarSalaPorId(salaId));
        verify(salaRepository).findById(salaId);
    }

    @Test
    void listarTodasSalas_DeveRetornarLista() {
        // Arrange
        SaladeAula sala1 = new SaladeAula();
        sala1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        sala1.setNome("Sala 1");

        SaladeAula sala2 = new SaladeAula();
        sala2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        sala2.setNome("Sala 2");

        List<SaladeAula> salas = List.of(sala1, sala2);

        when(salaRepository.findAll()).thenReturn(salas);

        // Act
        List<SaladeAula> result = salaService.listarTodasSalas();

        // Assert
        assertEquals(salas, result);
        verify(salaRepository).findAll();
    }

    @Test
    void criarSala_DeveRetornarSalaCriada_QuandoProfessor() {
        // Arrange
        Long criadorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario criador = new Professor(); // Professor extends Usuario
        criador.setId(criadorId);
        criador.setNome("Professor João");

        SaladeAula sala = new SaladeAula();
        sala.setNome("Nova Sala");

        SaladeAula salaSalva = new SaladeAula();
        salaSalva.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        salaSalva.setNome("Nova Sala");
        salaSalva.setCriador(criador);
        salaSalva.setCodigoSala("ABC12345");

        when(usuarioRepository.findById(criadorId)).thenReturn(Optional.of(criador));
        when(salaRepository.save(any(SaladeAula.class))).thenReturn(salaSalva);
        when(salaRepository.findByCodigoSala(anyString())).thenReturn(Optional.empty());

        // Act
        SaladeAula result = salaService.criarSala(sala, criadorId);

        // Assert
        assertNotNull(result);
        assertEquals("Nova Sala", result.getNome());
        verify(usuarioRepository).findById(criadorId);
        verify(salaRepository).save(any(SaladeAula.class));
    }

    @Test
    void criarSala_DeveLancarException_QuandoNaoProfessor() {
        // Arrange
        Long criadorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario criador = new Usuario(); // Não é Professor
        criador.setId(criadorId);
        criador.setNome("Aluno João");

        SaladeAula sala = new SaladeAula();
        sala.setNome("Nova Sala");

        when(usuarioRepository.findById(criadorId)).thenReturn(Optional.of(criador));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () ->
            salaService.criarSala(sala, criadorId));

        assertEquals("Apenas professores podem criar salas de aula.", exception.getMessage());
        verify(usuarioRepository).findById(criadorId);
    }

    @Test
    void deletarSala_DeveDeletar_QuandoCriador() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long userId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario criador = new Professor();
        criador.setId(userId);

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setCriador(criador);

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(sala));

        // Act
        salaService.deletarSala(salaId, userId);

        // Assert
        verify(salaRepository).findById(salaId);
        verify(salaRepository).deleteById(salaId);
    }

    @Test
    void deletarSala_DeveLancarException_QuandoNaoCriador() {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long userId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Usuario criador = new Professor();
        criador.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setCriador(criador);

        when(salaRepository.findById(salaId)).thenReturn(Optional.of(sala));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () ->
            salaService.deletarSala(salaId, userId));

        assertEquals("Apenas o criador da sala de aula pode realizar esta operação.", exception.getMessage());
        verify(salaRepository).findById(salaId);
    }
}