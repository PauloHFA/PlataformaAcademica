package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataSource dataSource;

    @Test
    void health_DeveRetornarStatusUp_QuandoChamado() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("Plataforma Acadêmica"));
    }

    @Test
    void databaseHealth_DeveRetornarStatusUp_QuandoConexaoValida() throws Exception {
        // Arrange
        Connection mockConnection = org.mockito.Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(2)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/health/db"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.database").value("PostgreSQL"));
    }

    @Test
    void databaseHealth_DeveRetornarStatusDown_QuandoConexaoInvalida() throws Exception {
        // Arrange
        Connection mockConnection = org.mockito.Mockito.mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(2)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/health/db"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOWN"));
    }

    @Test
    void info_DeveRetornarInformacoes_QuandoChamado() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Plataforma Acadêmica"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

    @Test
    void memoryHealth_DeveRetornarUsoMemoria_QuandoChamado() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health/memory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.max").exists())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.used").exists())
                .andExpect(jsonPath("$.free").exists())
                .andExpect(jsonPath("$.usage_percent").exists());
    }
}