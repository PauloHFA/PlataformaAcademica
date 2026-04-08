package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.FrequenciaRequestDTO;
import com.plataforma_academica.plataforma.model.Frequencia;
import com.plataforma_academica.plataforma.service.FrequenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FrequenciaControllerTest {

    @Mock
    private FrequenciaService frequenciaService;

    @InjectMocks
    private FrequenciaController frequenciaController;

    private Frequencia frequencia;
    private FrequenciaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        frequencia = new Frequencia();
        frequencia.setId(1L);

        requestDTO = new FrequenciaRequestDTO();
        requestDTO.setAlunoId(1L);
        requestDTO.setSalaId(1L);
        requestDTO.setData(LocalDate.now());
        requestDTO.setPresente(true);
        requestDTO.setJustificativa("Presente");
    }

    @Test
    void registrarFrequencia_DeveRetornarFrequenciaRegistrada() {
        // Arrange
        when(frequenciaService.registrarFrequencia(1L, 1L, LocalDate.now(), true, "Presente")).thenReturn(frequencia);

        // Act
        ResponseEntity<?> response = frequenciaController.registrarFrequencia(requestDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(frequencia, response.getBody());
        verify(frequenciaService, times(1)).registrarFrequencia(1L, 1L, LocalDate.now(), true, "Presente");
    }

    @Test
    void registrarFrequencia_CamposObrigatoriosAusentes_DeveRetornarBadRequest() {
        // Arrange
        requestDTO.setAlunoId(null);

        // Act
        ResponseEntity<?> response = frequenciaController.registrarFrequencia(requestDTO);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void buscarFrequencias_SemDatas_DeveRetornarLista() {
        // Arrange
        List<Frequencia> frequencias = Arrays.asList(frequencia);
        when(frequenciaService.buscarFrequencias(1L, 1L)).thenReturn(frequencias);

        // Act
        ResponseEntity<?> response = frequenciaController.buscarFrequencias(1L, 1L, null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(frequencias, response.getBody());
    }

    @Test
    void buscarFrequencias_ComDatas_DeveRetornarListaFiltrada() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        List<Frequencia> frequencias = Arrays.asList(frequencia);
        when(frequenciaService.buscarFrequencias(1L, 1L, inicio, fim)).thenReturn(frequencias);

        // Act
        ResponseEntity<?> response = frequenciaController.buscarFrequencias(1L, 1L, inicio.toString(), fim.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(frequencias, response.getBody());
    }

    @Test
    void percentualPresenca_DeveRetornarPercentual() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        when(frequenciaService.calcularPercentualPresenca(1L, 1L, inicio, fim)).thenReturn(85.0);

        // Act
        ResponseEntity<?> response = frequenciaController.percentualPresenca(1L, 1L, inicio.toString(), fim.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(85.0, response.getBody());
    }

    @Test
    void percentualPresenca_FormatoDataInvalido_DeveRetornarBadRequest() {
        // Act
        ResponseEntity<?> response = frequenciaController.percentualPresenca(1L, 1L, "invalid-date", "2023-12-31");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }
}