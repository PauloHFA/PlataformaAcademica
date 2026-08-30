package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.dto.MensagemDTO;
import com.plataforma_academica.plataforma.model.Mensagem;
import com.plataforma_academica.plataforma.repository.MensagemRepository;
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
 * Teste de Integração para MensagemController
 *
 * Usa H2 em memória e contexto Spring completo.
 * Perfeito para testar controllers com dependências reais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Garante rollback após cada teste
class MensagemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MensagemRepository mensagemRepository; // Para setup de dados se necessário

    @Test
    void enviarMensagem_DeveCriarMensagem_QuandoValida() throws Exception {
        MensagemDTO dto = new MensagemDTO();
        dto.setRemetenteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        dto.setConteudo("Olá, tudo bem?");

        mockMvc.perform(post("/api/mensagens/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo", is("Olá, tudo bem?")))
                .andExpect(jsonPath("$.remetenteId", is(1)))
                .andExpect(jsonPath("$.destinatarioId", is(2)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void obterMensagens_DeveRetornarListaVazia_QuandoNenhumaMensagem() throws Exception {
        mockMvc.perform(get("/api/mensagens/{usuarioId}/{amigoId}", UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void obterMensagens_DeveRetornarMensagens_QuandoExistem() throws Exception {
        // Setup: criar mensagens no banco de teste
        Mensagem msg1 = new Mensagem(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"), "Oi!");
        Mensagem msg2 = new Mensagem(UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000001"), "Oi, tudo bom?");
        mensagemRepository.save(msg1);
        mensagemRepository.save(msg2);

        mockMvc.perform(get("/api/mensagens/{usuarioId}/{amigoId}", UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].conteudo", is("Oi!")))
                .andExpect(jsonPath("$[1].conteudo", is("Oi, tudo bom?")));
    }

    @Test
    void obterMensagens_DeveRetornarMensagensOrdenadas_QuandoMultiplas() throws Exception {
        // Setup: criar mensagens em ordem cronológica
        Mensagem msg1 = new Mensagem(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"), "Primeira mensagem");
        msg1.setCriadoEm(java.time.LocalDateTime.now().minusMinutes(5));
        mensagemRepository.save(msg1);

        Mensagem msg2 = new Mensagem(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"), "Segunda mensagem");
        msg2.setCriadoEm(java.time.LocalDateTime.now().minusMinutes(3));
        mensagemRepository.save(msg2);

        Mensagem msg3 = new Mensagem(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002"), "Terceira mensagem");
        msg3.setCriadoEm(java.time.LocalDateTime.now().minusMinutes(1));
        mensagemRepository.save(msg3);

        mockMvc.perform(get("/api/mensagens/{usuarioId}/{amigoId}", UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].conteudo", is("Primeira mensagem")))
                .andExpect(jsonPath("$[1].conteudo", is("Segunda mensagem")))
                .andExpect(jsonPath("$[2].conteudo", is("Terceira mensagem")));
    }

    @Test
    void fluxoCompleto_DeveFuncionar_QuandoEnviandoEMostrandoMensagens() throws Exception {
        // Enviar primeira mensagem
        MensagemDTO dto1 = new MensagemDTO();
        dto1.setRemetenteId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto1.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        dto1.setConteudo("Mensagem inicial");

        mockMvc.perform(post("/api/mensagens/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Enviar segunda mensagem
        MensagemDTO dto2 = new MensagemDTO();
        dto2.setRemetenteId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        dto2.setDestinatarioId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto2.setConteudo("Resposta");

        mockMvc.perform(post("/api/mensagens/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk());

        // Verificar lista de mensagens entre os usuários
        mockMvc.perform(get("/api/mensagens/{usuarioId}/{amigoId}", UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].conteudo", is("Mensagem inicial")))
                .andExpect(jsonPath("$[1].conteudo", is("Resposta")));
    }
}