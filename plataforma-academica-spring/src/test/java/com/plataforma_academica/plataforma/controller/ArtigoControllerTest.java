package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.dto.ArtigoResponseDTO;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.ArtigoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtigoController.class)
class ArtigoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtigoService artigoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criar_DeveRetornarArtigoCriado_QuandoValido() throws Exception {
        // Arrange
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        ArtigoDTO request = new ArtigoDTO();
        request.setTitulo("Título do Artigo");
        request.setConteudo("Conteúdo do artigo");
        request.setAutorId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Artigo artigo = new Artigo();
        artigo.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        artigo.setTitulo("Título do Artigo");
        artigo.setConteudo("Conteúdo do artigo");
        artigo.setAutor(autor);
        artigo.setCriadoEm(LocalDateTime.now());

        when(artigoService.criar(any(ArtigoDTO.class))).thenReturn(artigo);

        // Act & Assert
        mockMvc.perform(post("/api/artigos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/artigos/1"))
                .andExpect(jsonPath("$.id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$.titulo").value("Título do Artigo"));
    }

    @Test
    void editar_DeveRetornarArtigoAtualizado_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        ArtigoDTO request = new ArtigoDTO();
        request.setTitulo("Título Atualizado");
        request.setConteudo("Conteúdo atualizado");

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setTitulo("Título Atualizado");
        artigo.setConteudo("Conteúdo atualizado");
        artigo.setAutor(autor);

        when(artigoService.editar(anyLong(), any(ArtigoDTO.class))).thenReturn(artigo);

        // Act & Assert
        mockMvc.perform(put("/api/artigos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título Atualizado"));
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoExiste() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long solicitanteId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Act & Assert
        mockMvc.perform(delete("/api/artigos/{id}", id)
                        .param("solicitanteId", solicitanteId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscar_DeveRetornarArtigo_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Artigo artigo = new Artigo();
        artigo.setId(id);
        artigo.setTitulo("Título do Artigo");
        artigo.setConteudo("Conteúdo");
        artigo.setAutor(autor);

        when(artigoService.buscarPorId(id)).thenReturn(artigo);

        // Act & Assert
        mockMvc.perform(get("/api/artigos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título do Artigo"));
    }

    @Test
    void listar_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Artigo artigo1 = new Artigo();
        artigo1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        artigo1.setTitulo("Artigo 1");
        artigo1.setAutor(autor);

        Artigo artigo2 = new Artigo();
        artigo2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        artigo2.setTitulo("Artigo 2");
        artigo2.setAutor(autor);

        List<Artigo> artigos = List.of(artigo1, artigo2);

        when(artigoService.listarTodos()).thenReturn(artigos);

        // Act & Assert
        mockMvc.perform(get("/api/artigos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Artigo 1"))
                .andExpect(jsonPath("$[1].titulo").value("Artigo 2"));
    }

    @Test
    void porAutor_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long autorId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(autorId);
        autor.setNome("João Silva");

        Artigo artigo = new Artigo();
        artigo.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        artigo.setTitulo("Artigo do Autor");
        artigo.setAutor(autor);

        List<Artigo> artigos = List.of(artigo);

        when(artigoService.listarPorAutor(autorId)).thenReturn(artigos);

        // Act & Assert
        mockMvc.perform(get("/api/artigos/autor/{autorId}", autorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Artigo do Autor"));
    }
}