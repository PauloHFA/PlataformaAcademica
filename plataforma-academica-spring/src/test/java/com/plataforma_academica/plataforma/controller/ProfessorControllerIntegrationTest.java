package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.Professor;
import com.plataforma_academica.plataforma.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de Integração para ProfessorController
 *
 * Usa H2 em memória e contexto Spring completo.
 * Perfeito para testar controllers com dependências reais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Garante rollback após cada teste
class ProfessorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfessorRepository professorRepository; // Para setup de dados se necessário

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void cadastrar_DeveCadastrarProfessor_QuandoValidoENovo() throws Exception {
        Professor professor = new Professor();
        professor.setNome("João");
        professor.setSobrenome("Silva");
        professor.setEmail("joao.silva@universidade.edu");
        professor.setSenha("senha123");
        professor.setMatricula("12345678");

        mockMvc.perform(post("/api/professores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João")))
                .andExpect(jsonPath("$.sobrenome", is("Silva")))
                .andExpect(jsonPath("$.email", is("joao.silva@universidade.edu")))
                .andExpect(jsonPath("$.matricula", is("12345678")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void cadastrar_DeveRetornarBadRequest_QuandoEmailJaExiste() throws Exception {
        // Setup: cadastrar professor primeiro
        Professor existente = new Professor();
        existente.setNome("Maria");
        existente.setEmail("maria.prof@universidade.edu");
        existente.setSenha(passwordEncoder.encode("senha123"));
        existente.setMatricula("87654321");
        professorRepository.save(existente);

        // Tentativa de cadastrar com mesmo email
        Professor novo = new Professor();
        novo.setNome("Maria");
        novo.setSobrenome("Oliveira");
        novo.setEmail("maria.prof@universidade.edu");
        novo.setSenha("senha456");
        novo.setMatricula("11223344");

        mockMvc.perform(post("/api/professores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email já cadastrado"));
    }

    @Test
    void login_DeveRetornarProfessor_QuandoCredenciaisCorretas() throws Exception {
        // Setup: cadastrar professor
        Professor professor = new Professor();
        professor.setNome("Carlos");
        professor.setEmail("carlos.prof@universidade.edu");
        professor.setSenha(passwordEncoder.encode("minhaSenha"));
        professor.setMatricula("11111111");
        Professor salvo = professorRepository.save(professor);

        // Login com credenciais corretas
        Professor loginData = new Professor();
        loginData.setEmail("carlos.prof@universidade.edu");
        loginData.setSenha("minhaSenha");

        mockMvc.perform(post("/api/professores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(salvo.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos.prof@universidade.edu")))
                .andExpect(jsonPath("$.matricula", is("11111111")));
    }

    @Test
    void login_DeveRetornar401_QuandoEmailIncorreto() throws Exception {
        // Setup: cadastrar professor
        Professor professor = new Professor();
        professor.setNome("Ana");
        professor.setEmail("ana.prof@universidade.edu");
        professor.setSenha(passwordEncoder.encode("senhaAna"));
        professor.setMatricula("22222222");
        professorRepository.save(professor);

        // Login com email incorreto
        Professor loginData = new Professor();
        loginData.setEmail("ana.errado@universidade.edu");
        loginData.setSenha("senhaAna");

        mockMvc.perform(post("/api/professores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email ou senha incorretos"));
    }

    @Test
    void login_DeveRetornar401_QuandoSenhaIncorreta() throws Exception {
        // Setup: cadastrar professor
        Professor professor = new Professor();
        professor.setNome("Roberto");
        professor.setEmail("roberto.prof@universidade.edu");
        professor.setSenha(passwordEncoder.encode("senhaCorreta"));
        professor.setMatricula("33333333");
        professorRepository.save(professor);

        // Login com senha incorreta
        Professor loginData = new Professor();
        loginData.setEmail("roberto.prof@universidade.edu");
        loginData.setSenha("senhaErrada");

        mockMvc.perform(post("/api/professores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email ou senha incorretos"));
    }

    @Test
    void fluxoCompleto_DeveCadastrarELogar_QuandoDadosValidos() throws Exception {
        // Cadastrar professor
        Professor professor = new Professor();
        professor.setNome("Fernanda");
        professor.setSobrenome("Costa");
        professor.setEmail("fernanda.costa@universidade.edu");
        professor.setSenha("fernanda123");
        professor.setMatricula("44444444");

        mockMvc.perform(post("/api/professores/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Fernanda")))
                .andExpect(jsonPath("$.matricula", is("44444444")));

        // Login com dados cadastrados
        Professor loginData = new Professor();
        loginData.setEmail("fernanda.costa@universidade.edu");
        loginData.setSenha("fernanda123");

        mockMvc.perform(post("/api/professores/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Fernanda")))
                .andExpect(jsonPath("$.email", is("fernanda.costa@universidade.edu")));
    }
}