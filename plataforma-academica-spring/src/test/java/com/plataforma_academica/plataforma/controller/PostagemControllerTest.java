package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;
import com.plataforma_academica.plataforma.service.PostagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostagemController.class)
class PostagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostagemService postagemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_DeveRetornarListaDePostagens_QuandoChamado() throws Exception {
        // Arrange
        List<PostagemResponseDTO> postagens = List.of(new PostagemResponseDTO());
        when(postagemService.listarTodasResponse()).thenReturn(postagens);

        // Act & Assert
        mockMvc.perform(get("/api/postagens"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void buscarPorId_DeveRetornarPostagem_QuandoEncontrada() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        PostagemResponseDTO postagem = new PostagemResponseDTO();
        postagem.setId(id);
        when(postagemService.buscarPorIdResponse(id)).thenReturn(postagem);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void buscarPorId_DeveRetornarNotFound_QuandoNaoEncontrada() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(postagemService.buscarPorIdResponse(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorTitulo_DeveRetornarLista_QuandoEncontrada() throws Exception {
        // Arrange
        String titulo = "teste";
        List<PostagemResponseDTO> resultados = List.of(new PostagemResponseDTO());
        when(postagemService.buscarPorTituloResponse(titulo)).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/titulo").param("titulo", titulo))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void buscarPorTitulo_DeveRetornarNoContent_QuandoVazia() throws Exception {
        // Arrange
        String titulo = "teste";
        List<PostagemResponseDTO> resultados = List.of();
        when(postagemService.buscarPorTituloResponse(titulo)).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/titulo").param("titulo", titulo))
                .andExpect(status().isNoContent());
    }

    @Test
    void publicar_DeveRetornarCreated_QuandoValido() throws Exception {
        // Arrange
        PostagemDTO request = new PostagemDTO();
        request.setTitulo("Título");
        request.setConteudo("Conteúdo");
        request.setAutorId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        PostagemResponseDTO response = new PostagemResponseDTO();
        response.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        when(postagemService.publicarResponse(any(PostagemDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/postagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/postagens/1"))
                .andExpect(jsonPath("$.id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")));
    }

    @Test
    void atualizar_DeveRetornarOk_QuandoValido() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        PostagemDTO request = new PostagemDTO();
        request.setId(id);
        request.setTitulo("Novo Título");

        PostagemResponseDTO response = new PostagemResponseDTO();
        response.setId(id);

        when(postagemService.atualizarResponse(any(PostagemDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/postagens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoValido() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Act & Assert
        mockMvc.perform(delete("/api/postagens/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void curtir_DeveRetornarOk_QuandoValido() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        PostagemResponseDTO response = new PostagemResponseDTO();
        response.setId(id);

        when(postagemService.curtir(id, usuarioId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/postagens/{id}/curtir", id)
                        .param("usuarioId", usuarioId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void listarDeAmigos_DeveRetornarLista_QuandoValido() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<PostagemResponseDTO> postagens = List.of(new PostagemResponseDTO());

        when(postagemService.listarDeAmigos(usuarioId)).thenReturn(postagens);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/amigos/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void listarMaisCurtidas_DeveRetornarLista_QuandoValido() throws Exception {
        // Arrange
        List<PostagemResponseDTO> postagens = List.of(new PostagemResponseDTO());

        when(postagemService.listarMaisCurtidas()).thenReturn(postagens);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/mais-curtidas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void verificarCurtida_DeveRetornarBoolean_QuandoValido() throws Exception {
        // Arrange
        Long postagemId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(postagemService.verificarCurtida(postagemId, usuarioId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/postagens/{postagemId}/curtiu/{usuarioId}", postagemId, usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}