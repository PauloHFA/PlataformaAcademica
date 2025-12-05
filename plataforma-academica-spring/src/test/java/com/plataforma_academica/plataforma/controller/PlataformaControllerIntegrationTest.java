package com.plataforma_academica.plataforma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de Integração para PlataformaController
 *
 * Usa H2 em memória e contexto Spring completo.
 * Perfeito para testar controllers com dependências reais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Garante rollback após cada teste
class PlataformaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlataformaRepository plataformaRepository; // Para setup de dados se necessário

    @Test
    void listar_DeveRetornarListaVazia_QuandoNenhumaPlataforma() throws Exception {
        mockMvc.perform(get("/api/plataforma"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void criar_DeveCriarPlataforma_QuandoValida() throws Exception {
        Plataforma novaPlataforma = new Plataforma();
        novaPlataforma.setNome("Biblioteca Virtual");

        mockMvc.perform(post("/api/plataforma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaPlataforma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Biblioteca Virtual")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void buscarPorId_DeveRetornarPlataforma_QuandoEncontrada() throws Exception {
        // Setup: criar uma plataforma no banco de teste
        Plataforma plataforma = new Plataforma();
        plataforma.setNome("Fórum Acadêmico");
        Plataforma salva = plataformaRepository.save(plataforma);

        mockMvc.perform(get("/api/plataforma/{id}", salva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Fórum Acadêmico")))
                .andExpect(jsonPath("$.id", is(salva.getId().intValue())));
    }

    @Test
    void buscarPorId_DeveRetornar404_QuandoNaoEncontrada() throws Exception {
        mockMvc.perform(get("/api/plataforma/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_DeveAtualizarPlataforma_QuandoEncontrada() throws Exception {
        // Setup: criar uma plataforma
        Plataforma plataforma = new Plataforma();
        plataforma.setNome("Nome Antigo");
        Plataforma salva = plataformaRepository.save(plataforma);

        // Dados para atualização
        Plataforma atualizada = new Plataforma();
        atualizada.setNome("Nome Novo");

        mockMvc.perform(put("/api/plataforma/{id}", salva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Nome Novo")))
                .andExpect(jsonPath("$.id", is(salva.getId().intValue())));
    }

    @Test
    void deletar_DeveRemoverPlataforma_QuandoEncontrada() throws Exception {
        // Setup: criar uma plataforma
        Plataforma plataforma = new Plataforma();
        plataforma.setNome("Plataforma Para Deletar");
        Plataforma salva = plataformaRepository.save(plataforma);

        // Deletar
        mockMvc.perform(delete("/api/plataforma/{id}", salva.getId()))
                .andExpect(status().isNoContent());

        // Verificar que foi removida
        mockMvc.perform(get("/api/plataforma/{id}", salva.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_DeveFuncionar_QuandoPlataformaNaoExiste() throws Exception {
        // Como o service não lança exceção para ID inexistente, deve retornar 204
        mockMvc.perform(delete("/api/plataforma/{id}", 999L))
                .andExpect(status().isNoContent());
    }

    @Test
    void fluxoCompleto_DeveFuncionar_QuandoCriandoEListando() throws Exception {
        // Criar primeira plataforma
        Plataforma plataforma1 = new Plataforma();
        plataforma1.setNome("Central de Atividades");

        mockMvc.perform(post("/api/plataforma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plataforma1)))
                .andExpect(status().isOk());

        // Criar segunda plataforma
        Plataforma plataforma2 = new Plataforma();
        plataforma2.setNome("Catálogo de Cursos");

        mockMvc.perform(post("/api/plataforma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(plataforma2)))
                .andExpect(status().isOk());

        // Listar todas - deve ter 2 plataformas
        mockMvc.perform(get("/api/plataforma"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", notNullValue()))
                .andExpect(jsonPath("$[1].nome", notNullValue()));
    }
}