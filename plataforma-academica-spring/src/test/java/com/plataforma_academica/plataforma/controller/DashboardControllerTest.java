package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.service.DashboardAlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardControllerTest {

    @Mock
    private DashboardAlunoService dashboardAlunoService;

    @InjectMocks
    private DashboardController dashboardController;

    private DashboardAlunoDTO dashboardDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dashboardDTO = new DashboardAlunoDTO();
        dashboardDTO.setAlunoId(1L);
        dashboardDTO.setSalaId(1L);
    }

    @Test
    void getDashboardAluno_DeveRetornarDashboard() {
        // Arrange
        when(dashboardAlunoService.obterDashboardAluno(1L, 1L, null, null)).thenReturn(dashboardDTO);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(1L, 1L, null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dashboardDTO, response.getBody());
        verify(dashboardAlunoService, times(1)).obterDashboardAluno(1L, 1L, null, null);
    }

    @Test
    void getDashboardAluno_ComDatas_DevePassarDatas() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        when(dashboardAlunoService.obterDashboardAluno(1L, 1L, inicio, fim)).thenReturn(dashboardDTO);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(1L, 1L, inicio.toString(), fim.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dashboardDTO, response.getBody());
        verify(dashboardAlunoService, times(1)).obterDashboardAluno(1L, 1L, inicio, fim);
    }

    @Test
    void getDashboardAluno_FormatoDataInvalido_DeveRetornarBadRequest() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(1L, 1L, "invalid-date", "2023-12-31");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Formato de data inválido"));
    }
}