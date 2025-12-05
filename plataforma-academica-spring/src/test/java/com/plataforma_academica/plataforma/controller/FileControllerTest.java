package com.plataforma_academica.plataforma.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void serveFile_DeveRetornarNotFound_QuandoArquivoNaoExiste() throws Exception {
        // Arrange
        String filename = "nonexistent.png";

        // Act & Assert
        mockMvc.perform(get("/uploads/{filename}", filename))
                .andExpect(status().isNotFound());
    }
}