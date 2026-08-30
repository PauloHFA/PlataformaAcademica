package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.DashboardSalaDTO;
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
        dashboardDTO.setAlunoId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dashboardDTO.setSalaId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void getDashboardAluno_DeveRetornarDashboard() {
        // Arrange
        when(dashboardAlunoService.obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null)).thenReturn(dashboardDTO);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dashboardDTO, response.getBody());
        verify(dashboardAlunoService, times(1)).obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);
    }

    @Test
    void getDashboardAluno_ComDatas_DevePassarDatas() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(7);
        LocalDate fim = LocalDate.now();
        when(dashboardAlunoService.obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), inicio, fim)).thenReturn(dashboardDTO);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), inicio.toString(), fim.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dashboardDTO, response.getBody());
        verify(dashboardAlunoService, times(1)).obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), inicio, fim);
    }

    @Test
    void getDashboardSala_DeveRetornarDashboardSala() {
        // Arrange
        DashboardSalaDTO salaDTO = new DashboardSalaDTO();
        salaDTO.setSalaId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        salaDTO.setSalaNome("Sala de POO");
        when(dashboardAlunoService.obterDashboardSala(UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null)).thenReturn(salaDTO);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboardSala(UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(salaDTO, response.getBody());
        verify(dashboardAlunoService, times(1)).obterDashboardSala(UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);
    }

    @Test
    void getDashboardAluno_FormatoDataInvalido_DeveRetornarBadRequest() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), "invalid-date", "2023-12-31");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Formato de data inválido"));
    }

    @Test
    void getDashboardSala_FormatoDataInvalido_DeveRetornarBadRequest() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboardSala(UUID.fromString("00000000-0000-0000-0000-000000000001"), "invalid-date", "2023-12-31");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Formato de data inválido"));
    }
}