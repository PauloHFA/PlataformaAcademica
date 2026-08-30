package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.ComunidadeDTO;
import com.plataforma_academica.plataforma.model.Comunidade;
import com.plataforma_academica.plataforma.model.MembroComunidade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.ComunidadeService;
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

@WebMvcTest(ComunidadeController.class)
class ComunidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComunidadeService comunidadeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criar_DeveRetornarComunidadeCriada_QuandoValido() throws Exception {
        // Arrange
        Usuario dono = new Usuario();
        dono.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dono.setNome("João Silva");

        ComunidadeDTO request = new ComunidadeDTO();
        request.setNome("Comunidade de Matemática");
        request.setDescricao("Discussões sobre matemática");
        request.setDonoId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Comunidade comunidade = new Comunidade();
        comunidade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade.setNome("Comunidade de Matemática");
        comunidade.setDescricao("Discussões sobre matemática");
        comunidade.setDono(dono);
        comunidade.setCriadoEm(LocalDateTime.now());

        when(comunidadeService.criarComunidade(any(ComunidadeDTO.class))).thenReturn(comunidade);

        // Act & Assert
        mockMvc.perform(post("/api/comunidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/comunidades/1"))
                .andExpect(jsonPath("$.nome").value("Comunidade de Matemática"));
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoExiste() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long solicitanteId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // Act & Assert
        mockMvc.perform(delete("/api/comunidades/{id}", id)
                        .param("solicitanteId", solicitanteId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void entrar_DeveRetornarMembroComunidade_QuandoValido() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Maria Santos");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(id);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setComunidade(comunidade);
        membro.setUsuario(usuario);
        membro.setPapel("MEMBRO");

        when(comunidadeService.entrarComunidade(id, usuarioId)).thenReturn(membro);

        // Act & Assert
        mockMvc.perform(post("/api/comunidades/{id}/entrar", id)
                        .param("usuarioId", usuarioId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.papel").value("MEMBRO"));
    }

    @Test
    void sair_DeveRetornarNoContent_QuandoExiste() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Long usuarioId = UUID.fromString("00000000-0000-0000-0000-000000000002");

        // Act & Assert
        mockMvc.perform(post("/api/comunidades/{id}/sair", id)
                        .param("usuarioId", usuarioId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    void listar_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Usuario dono = new Usuario();
        dono.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dono.setNome("João Silva");

        Comunidade comunidade1 = new Comunidade();
        comunidade1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comunidade1.setNome("Comunidade 1");
        comunidade1.setDono(dono);

        Comunidade comunidade2 = new Comunidade();
        comunidade2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        comunidade2.setNome("Comunidade 2");
        comunidade2.setDono(dono);

        List<Comunidade> comunidades = List.of(comunidade1, comunidade2);

        when(comunidadeService.listarTodas()).thenReturn(comunidades);

        // Act & Assert
        mockMvc.perform(get("/api/comunidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Comunidade 1"))
                .andExpect(jsonPath("$[1].nome").value("Comunidade 2"));
    }

    @Test
    void listarMembros_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        usuario.setNome("João Silva");

        Comunidade comunidade = new Comunidade();
        comunidade.setId(id);
        comunidade.setNome("Comunidade Teste");

        MembroComunidade membro = new MembroComunidade();
        membro.setComunidade(comunidade);
        membro.setUsuario(usuario);
        membro.setPapel("ADMIN");

        List<MembroComunidade> membros = List.of(membro);

        when(comunidadeService.listarMembros(id)).thenReturn(membros);

        // Act & Assert
        mockMvc.perform(get("/api/comunidades/{id}/membros", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].papel").value("ADMIN"));
    }
}