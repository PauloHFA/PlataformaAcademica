package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.PerfilDTO;
import com.plataforma_academica.plataforma.model.Perfil;
import com.plataforma_academica.plataforma.service.PerfilService;
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

@WebMvcTest(PerfilController.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfilService perfilService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_DeveRetornarListaDePerfis_QuandoChamado() throws Exception {
        // Arrange
        Perfil perfil1 = new Perfil();
        perfil1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        perfil1.setNome("João Silva");
        perfil1.setBio("Bio do João");

        Perfil perfil2 = new Perfil();
        perfil2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        perfil2.setNome("Maria Santos");
        perfil2.setBio("Bio da Maria");

        List<Perfil> perfis = List.of(perfil1, perfil2);

        when(perfilService.listarTodos()).thenReturn(perfis);

        // Act & Assert
        mockMvc.perform(get("/api/perfis"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].bio").value("Bio do João"))
                .andExpect(jsonPath("$[1].id").value(UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"))
                .andExpect(jsonPath("$[1].bio").value("Bio da Maria"));
    }

    @Test
    void criar_DeveRetornarPerfilCriado_QuandoValido() throws Exception {
        // Arrange
        PerfilDTO request = new PerfilDTO();
        request.setUsuarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        request.setBio("Nova bio");
        request.setCurso("Engenharia");
        request.setFotoPerfil("foto.jpg");

        Perfil perfilCriado = new Perfil();
        perfilCriado.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        perfilCriado.setNome("João Silva");
        perfilCriado.setBio("Nova bio");
        perfilCriado.setCurso("Engenharia");
        perfilCriado.setFotoPerfil("foto.jpg");

        when(perfilService.salvar(any(PerfilDTO.class))).thenReturn(perfilCriado);

        // Act & Assert
        mockMvc.perform(post("/api/perfis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.bio").value("Nova bio"))
                .andExpect(jsonPath("$.curso").value("Engenharia"))
                .andExpect(jsonPath("$.fotoPerfil").value("foto.jpg"));
    }

    @Test
    void buscarPorId_DeveRetornarPerfil_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Perfil perfil = new Perfil();
        perfil.setId(id);
        perfil.setNome("João Silva");
        perfil.setBio("Bio do João");
        perfil.setCurso("Engenharia");

        when(perfilService.buscarPorId(id)).thenReturn(perfil);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.bio").value("Bio do João"))
                .andExpect(jsonPath("$.curso").value("Engenharia"));
    }

    @Test
    void buscarPorId_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(perfilService.buscarPorId(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_DeveRetornarPerfilAtualizado_QuandoValido() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        PerfilDTO request = new PerfilDTO();
        request.setBio("Bio atualizada");
        request.setCurso("Novo curso");

        Perfil perfilAtualizado = new Perfil();
        perfilAtualizado.setId(id);
        perfilAtualizado.setNome("João Silva");
        perfilAtualizado.setBio("Bio atualizada");
        perfilAtualizado.setCurso("Novo curso");

        when(perfilService.atualizar(anyLong(), any(PerfilDTO.class))).thenReturn(perfilAtualizado);

        // Act & Assert
        mockMvc.perform(put("/api/perfis/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.bio").value("Bio atualizada"))
                .andExpect(jsonPath("$.curso").value("Novo curso"));
    }

    @Test
    void buscarPorCurso_DeveRetornarListaDePerfis_QuandoEncontrados() throws Exception {
        // Arrange
        String curso = "Engenharia";
        Perfil perfil1 = new Perfil();
        perfil1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        perfil1.setNome("João Silva");
        perfil1.setCurso(curso);

        Perfil perfil2 = new Perfil();
        perfil2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        perfil2.setNome("Maria Santos");
        perfil2.setCurso(curso);

        List<Perfil> perfis = List.of(perfil1, perfil2);

        when(perfilService.buscarPorCurso(curso)).thenReturn(perfis);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/curso/{curso}", curso))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].curso").value(curso))
                .andExpect(jsonPath("$[1].id").value(UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(jsonPath("$[1].curso").value(curso));
    }

    @Test
    void buscarPorUsuario_DeveRetornarPerfil_QuandoEncontrado() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Perfil perfil = new Perfil();
        perfil.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        perfil.setNome("João Silva");
        perfil.setBio("Bio do João");

        when(perfilService.buscarPorUsuarioId(usuarioId)).thenReturn(perfil);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.bio").value("Bio do João"));
    }

    @Test
    void buscarPorUsuario_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(perfilService.buscarPorUsuarioId(usuarioId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isNotFound());
    }

    @Test
    void existePerfil_DeveRetornarTrue_QuandoPerfilExiste() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(perfilService.existePerfilDoUsuario(usuarioId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/existe/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existePerfil_DeveRetornarFalse_QuandoPerfilNaoExiste() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(perfilService.existePerfilDoUsuario(usuarioId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/perfis/existe/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}