package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
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
class PlataformaServiceImplTest {

    @Mock
    private PlataformaRepository plataformaRepository;

    @InjectMocks
    private PlataformaServiceImpl plataformaService;

    @Test
    void salvar_DeveRetornarPlataformaSalva_QuandoChamado() {
        // Arrange
        Plataforma plataforma = new Plataforma();
        plataforma.setNome("Plataforma Teste");

        Plataforma plataformaSalva = new Plataforma();
        plataformaSalva.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        plataformaSalva.setNome("Plataforma Teste");

        when(plataformaRepository.save(any(Plataforma.class))).thenReturn(plataformaSalva);

        // Act
        Plataforma result = plataformaService.salvar(plataforma);

        // Assert
        assertNotNull(result);
        assertEquals("Plataforma Teste", result.getNome());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result.getId());
        verify(plataformaRepository).save(any(Plataforma.class));
    }

    @Test
    void atualizar_DeveRetornarPlataformaAtualizada_QuandoEncontrada() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Plataforma existente = new Plataforma();
        existente.setId(id);
        existente.setNome("Nome Antigo");

        Plataforma atualizadaRequest = new Plataforma();
        atualizadaRequest.setNome("Nome Novo");

        Plataforma atualizadaSalva = new Plataforma();
        atualizadaSalva.setId(id);
        atualizadaSalva.setNome("Nome Novo");

        when(plataformaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(plataformaRepository.save(any(Plataforma.class))).thenReturn(atualizadaSalva);

        // Act
        Plataforma result = plataformaService.atualizar(id, atualizadaRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Nome Novo", result.getNome());
        assertEquals(id, result.getId());
        verify(plataformaRepository).findById(id);
        verify(plataformaRepository).save(any(Plataforma.class));
    }

    @Test
    void atualizar_DeveLancarException_QuandoNaoEncontrada() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Plataforma atualizadaRequest = new Plataforma();
        atualizadaRequest.setNome("Nome Novo");

        when(plataformaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            plataformaService.atualizar(id, atualizadaRequest));

        assertEquals("Plataforma não encontrada", exception.getMessage());
        verify(plataformaRepository).findById(id);
        verify(plataformaRepository, never()).save(any(Plataforma.class));
    }

    @Test
    void buscarPorId_DeveRetornarPlataforma_QuandoEncontrada() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Plataforma plataforma = new Plataforma();
        plataforma.setId(id);
        plataforma.setNome("Plataforma Teste");

        when(plataformaRepository.findById(id)).thenReturn(Optional.of(plataforma));

        // Act
        Plataforma result = plataformaService.buscarPorId(id);

        // Assert
        assertEquals(plataforma, result);
        verify(plataformaRepository).findById(id);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrada() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(plataformaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Plataforma result = plataformaService.buscarPorId(id);

        // Assert
        assertNull(result);
        verify(plataformaRepository).findById(id);
    }

    @Test
    void listarTudo_DeveRetornarLista() {
        // Arrange
        Plataforma plataforma1 = new Plataforma();
        plataforma1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        plataforma1.setNome("Plataforma 1");

        Plataforma plataforma2 = new Plataforma();
        plataforma2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        plataforma2.setNome("Plataforma 2");

        List<Plataforma> plataformas = List.of(plataforma1, plataforma2);

        when(plataformaRepository.findAll()).thenReturn(plataformas);

        // Act
        List<Plataforma> result = plataformaService.listarTudo();

        // Assert
        assertEquals(plataformas, result);
        verify(plataformaRepository).findAll();
    }

    @Test
    void deletar_DeveDeletarPorId() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Act
        plataformaService.deletar(id);

        // Assert
        verify(plataformaRepository).deleteById(id);
    }
}