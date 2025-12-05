package com.plataforma_academica.plataforma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.AtividadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtividadeController.class)
class AtividadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtividadeService atividadeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarAtividade_DeveRetornarAtividadeCriada_QuandoValido() throws Exception {
        // Arrange
        Long salaId = 1L;
        Long autorId = 1L;
        Usuario autor = new Usuario();
        autor.setId(autorId);
        autor.setNome("Professor Silva");

        SaladeAula sala = new SaladeAula();
        sala.setId(salaId);
        sala.setNome("Sala de Matemática");

        AtividadeDTO request = new AtividadeDTO();
        request.setTitulo("Atividade de Matemática");
        request.setDescricao("Resolva os exercícios");
        request.setTipoDocumentoSubmissao("PDF");
        request.setDataEntrega("2023-12-31");
        request.setPontos(10.0);

        Atividade atividade = new Atividade();
        atividade.setId(1L);
        atividade.setTitulo("Atividade de Matemática");
        atividade.setDescricao("Resolva os exercícios");
        atividade.setTipoDocumentoSubmissao("PDF");
        atividade.setDataEntrega(LocalDate.of(2023, 12, 31));
        atividade.setPontos(10.0);
        atividade.setAutor(autor);
        atividade.setSalaDeAula(sala);

        when(atividadeService.criarAtividade(salaId, request, autorId)).thenReturn(atividade);

        // Act & Assert
        mockMvc.perform(post("/atividades/sala/{salaId}/autor/{autorId}", salaId, autorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atividade de Matemática"));
    }

    @Test
    void buscarAtividadePorId_DeveRetornarAtividade_QuandoEncontrada() throws Exception {
        // Arrange
        Long atividadeId = 1L;
        Usuario autor = new Usuario();
        autor.setId(1L);
        autor.setNome("Professor Silva");

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);
        atividade.setTitulo("Atividade Teste");
        atividade.setAutor(autor);

        when(atividadeService.buscarAtividadePorId(atividadeId)).thenReturn(atividade);

        // Act & Assert
        mockMvc.perform(get("/atividades/{atividadeId}", atividadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atividade Teste"));
    }

    @Test
    void listarPorSala_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long salaId = 1L;
        Usuario autor = new Usuario();
        autor.setId(1L);
        autor.setNome("Professor Silva");

        Atividade atividade1 = new Atividade();
        atividade1.setId(1L);
        atividade1.setTitulo("Atividade 1");
        atividade1.setAutor(autor);

        Atividade atividade2 = new Atividade();
        atividade2.setId(2L);
        atividade2.setTitulo("Atividade 2");
        atividade2.setAutor(autor);

        List<Atividade> atividades = List.of(atividade1, atividade2);

        when(atividadeService.listarAtividadesPorSala(salaId)).thenReturn(atividades);

        // Act & Assert
        mockMvc.perform(get("/atividades/sala/{salaId}", salaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Atividade 1"))
                .andExpect(jsonPath("$[1].titulo").value("Atividade 2"));
    }

    @Test
    void listarPorAutor_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long autorId = 1L;
        Usuario autor = new Usuario();
        autor.setId(autorId);
        autor.setNome("Professor Silva");

        Atividade atividade = new Atividade();
        atividade.setId(1L);
        atividade.setTitulo("Atividade do Autor");
        atividade.setAutor(autor);

        List<Atividade> atividades = List.of(atividade);

        when(atividadeService.listarAtividadesPorAutor(autorId)).thenReturn(atividades);

        // Act & Assert
        mockMvc.perform(get("/atividades/autor/{autorId}", autorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Atividade do Autor"));
    }

    @Test
    void atualizarAtividade_DeveRetornarAtividadeAtualizada_QuandoEncontrada() throws Exception {
        // Arrange
        Long atividadeId = 1L;
        Long autorId = 1L;
        Usuario autor = new Usuario();
        autor.setId(autorId);
        autor.setNome("Professor Silva");

        AtividadeDTO request = new AtividadeDTO();
        request.setTitulo("Atividade Atualizada");
        request.setDescricao("Descrição atualizada");

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);
        atividade.setTitulo("Atividade Atualizada");
        atividade.setDescricao("Descrição atualizada");
        atividade.setAutor(autor);

        when(atividadeService.atualizarAtividade(atividadeId, request, autorId)).thenReturn(atividade);

        // Act & Assert
        mockMvc.perform(put("/atividades/{atividadeId}/autor/{autorId}", atividadeId, autorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atividade Atualizada"));
    }

    @Test
    void deletarAtividade_DeveRetornarNoContent_QuandoExiste() throws Exception {
        // Arrange
        Long atividadeId = 1L;
        Long autorId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/atividades/{atividadeId}/autor/{autorId}", atividadeId, autorId))
                .andExpect(status().isNoContent());
    }
}