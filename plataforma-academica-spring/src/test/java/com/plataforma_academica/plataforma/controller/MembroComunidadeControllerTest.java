package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.MembroComunidadeService;
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

@WebMvcTest(controllers = MembroComunidadeController.class)
class MembroComunidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MembroComunidadeService membroComunidadeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodos_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        List<MembroComunidade> membros = List.of(membro);

        when(membroComunidadeService.listarTodos()).thenReturn(membros);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].papel").value("MEMBRO"));
    }

    @Test
    void buscarPorId_DeveRetornarMembro_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(id);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeService.buscarPorId(id)).thenReturn(membro);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.papel").value("MEMBRO"));
    }

    @Test
    void buscarPorId_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(membroComunidadeService.buscarPorId(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorUsuario_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        List<MembroComunidade> membros = List.of(membro);

        when(membroComunidadeService.buscarPorUsuario(usuarioId)).thenReturn(membros);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/usuario/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].papel").value("MEMBRO"));
    }

    @Test
    void buscarPorComunidade_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        List<MembroComunidade> membros = List.of(membro);

        when(membroComunidadeService.buscarPorComunidade(comunidadeId)).thenReturn(membros);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/comunidade/{comunidadeId}", comunidadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].papel").value("MEMBRO"));
    }

    @Test
    void buscarPorUsuarioEComunidade_DeveRetornarMembro_QuandoEncontrado() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(comunidadeId);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeService.buscarPorUsuarioEComunidade(usuarioId, comunidadeId)).thenReturn(membro);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/existe")
                        .param("usuarioId", usuarioId.toString())
                        .param("comunidadeId", comunidadeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.papel").value("MEMBRO"));
    }

    @Test
    void buscarPorUsuarioEComunidade_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long comunidadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(membroComunidadeService.buscarPorUsuarioEComunidade(usuarioId, comunidadeId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/membrocomunidade/existe")
                        .param("usuarioId", usuarioId.toString())
                        .param("comunidadeId", comunidadeId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void adicionar_DeveRetornarCreated_QuandoNovo() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade request = new MembroComunidade();
        request.setUsuario(usuario);
        request.setComunidade(comunidade);
        request.setPapel("MEMBRO");

        MembroComunidade salvo = new MembroComunidade();
        salvo.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        salvo.setUsuario(usuario);
        salvo.setComunidade(comunidade);
        salvo.setPapel("MEMBRO");
        salvo.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeService.buscarPorUsuarioEComunidade(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(null);
        when(membroComunidadeService.salvar(any(MembroComunidade.class))).thenReturn(salvo);

        // Act & Assert
        mockMvc.perform(post("/api/membrocomunidade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.papel").value("MEMBRO"));
    }

    @Test
    void adicionar_DeveRetornarConflict_QuandoJaExiste() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade request = new MembroComunidade();
        request.setUsuario(usuario);
        request.setComunidade(comunidade);
        request.setPapel("MEMBRO");

        MembroComunidade existente = new MembroComunidade();
        existente.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        existente.setUsuario(usuario);
        existente.setComunidade(comunidade);
        existente.setPapel("MEMBRO");
        existente.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeService.buscarPorUsuarioEComunidade(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(existente);

        // Act & Assert
        mockMvc.perform(post("/api/membrocomunidade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setId(id);
        membro.setUsuario(usuario);
        membro.setComunidade(comunidade);
        membro.setPapel("MEMBRO");
        membro.setEntrouEm(LocalDateTime.now());

        when(membroComunidadeService.buscarPorId(id)).thenReturn(membro);

        // Act & Assert
        mockMvc.perform(delete("/api/membrocomunidade/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(membroComunidadeService.buscarPorId(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/api/membrocomunidade/{id}", id))
                .andExpect(status().isNotFound());
    }
}