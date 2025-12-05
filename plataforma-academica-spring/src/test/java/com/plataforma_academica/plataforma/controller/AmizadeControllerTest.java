package com.plataforma_academica.plataforma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.dto.AmizadeResponseDTO;
import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.AmizadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AmizadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AmizadeService amizadeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void enviarSolicitacao_DeveRetornarAmizadeCriada_QuandoValido() throws Exception {
        // Arrange
        Usuario solicitante = new Usuario();
        solicitante.setId(1L);
        solicitante.setNome("João Silva");

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");

        AmizadeDTO request = new AmizadeDTO();
        request.setSolicitanteId(1L);
        request.setDestinatarioId(2L);

        Amizade amizade = new Amizade();
        amizade.setId(1L);
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.PENDENTE);
        amizade.setCriadoEm(LocalDateTime.now());

        when(amizadeService.enviarSolicitacao(any(AmizadeDTO.class))).thenReturn(amizade);

        // Act & Assert
        mockMvc.perform(post("/api/amizades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/amizades/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void responderSolicitacao_DeveAceitar_QuandoAcaoAceitar() throws Exception {
        // Arrange
        Long id = 1L;
        String acao = "aceitar";

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);
        solicitante.setNome("João Silva");

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");

        Amizade amizade = new Amizade();
        amizade.setId(id);
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.ACEITO);

        when(amizadeService.responderSolicitacao(id, acao)).thenReturn(amizade);

        // Act & Assert
        mockMvc.perform(patch("/api/amizades/{id}/resposta", id)
                        .param("acao", acao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACEITO"));
    }

    @Test
    void responderSolicitacao_DeveRecusar_QuandoAcaoRecusar() throws Exception {
        // Arrange
        Long id = 1L;
        String acao = "recusar";

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);
        solicitante.setNome("João Silva");

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");

        Amizade amizade = new Amizade();
        amizade.setId(id);
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.RECUSADO);

        when(amizadeService.responderSolicitacao(id, acao)).thenReturn(amizade);

        // Act & Assert
        mockMvc.perform(patch("/api/amizades/{id}/resposta", id)
                        .param("acao", acao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECUSADO"));
    }

    @Test
    void removerAmizade_DeveRetornarNoContent_QuandoExiste() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/amizades/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarPendentes_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long usuarioId = 1L;

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);
        solicitante.setNome("João Silva");

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");

        Amizade amizade = new Amizade();
        amizade.setId(1L);
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.PENDENTE);

        List<Amizade> pendentes = List.of(amizade);

        when(amizadeService.listarSolicitacoesPendentes(usuarioId)).thenReturn(pendentes);

        // Act & Assert
        mockMvc.perform(get("/api/amizades/pendentes/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDENTE"));
    }

    @Test
    void listarAmigos_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long usuarioId = 1L;

        Usuario solicitante = new Usuario();
        solicitante.setId(1L);
        solicitante.setNome("João Silva");

        Usuario destinatario = new Usuario();
        destinatario.setId(2L);
        destinatario.setNome("Maria Santos");

        Amizade amizade = new Amizade();
        amizade.setId(1L);
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.ACEITO);

        List<Amizade> amigos = List.of(amizade);

        when(amizadeService.listarAmigos(usuarioId)).thenReturn(amigos);

        // Act & Assert
        mockMvc.perform(get("/api/amizades/amigos/{usuarioId}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("ACEITO"));
    }
}