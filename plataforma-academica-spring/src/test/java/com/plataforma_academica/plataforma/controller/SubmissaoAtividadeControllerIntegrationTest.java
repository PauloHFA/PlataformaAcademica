package com.plataforma_academica.plataforma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.SubmissaoAtividadeRespository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
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
 * Teste de Integração para SubmissaoAtividadeController
 *
 * Usa H2 em memória e contexto Spring completo.
 * Perfeito para testar controllers com dependências reais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Garante rollback após cada teste
class SubmissaoAtividadeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubmissaoAtividadeRespository submissaoRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private SaladeAulaRepository salaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void listarPorAtividade_DeveRetornarListaVazia_QuandoNenhumaSubmissao() throws Exception {
        // Setup: criar atividade
        Usuario criador = new Usuario();
        criador.setNome("Prof Sub");
        criador.setEmail("prof.sub@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Submissões");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("SUB12345");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Teste");
        atividade.setDescricao("Descrição teste");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        mockMvc.perform(get("/api/submissaoatividade/atividade/{atividadeId}", atividadeSalva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void enviarSubmissao_DeveCriarSubmissao_QuandoValida() throws Exception {
        // Setup: sala, atividade, aluno
        Usuario criador = new Usuario();
        criador.setNome("Prof Env");
        criador.setEmail("prof.env@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        Usuario aluno = new Usuario();
        aluno.setNome("Aluno Env");
        aluno.setEmail("aluno.env@edu.com");
        aluno.setSenha("123");
        Usuario alunoSalvo = usuarioRepository.save(aluno);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Envio");
        sala.setCriador(criadorSalvo);
        sala.setUsuarios(java.util.Arrays.asList(alunoSalvo));
        sala.setCodigoSala("ENV67890");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Envio");
        atividade.setDescricao("Enviar trabalho");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        // Enviar submissão (sem arquivo para teste)
        mockMvc.perform(multipart("/api/submissaoatividade/atividade/{atividadeId}/aluno/{alunoId}",
                        atividadeSalva.getId(), alunoSalvo.getId())
                        .param("descricao", "Minha submissão"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is("Minha submissão")))
                .andExpect(jsonPath("$.aluno.nome", is("Aluno Env")))
                .andExpect(jsonPath("$.atividade.titulo", is("Atividade Envio")))
                .andExpect(jsonPath("$.dataEnvio", notNullValue()));
    }

    @Test
    void buscarSubmissaoAluno_DeveRetornarSubmissao_QuandoExiste() throws Exception {
        // Para este teste, seria necessário ter uma submissão já criada
        // Como o service cria, assumimos que funciona se enviar primeiro

        // Criar setup e enviar via endpoint
        Usuario criador = new Usuario();
        criador.setNome("Prof Bus");
        criador.setEmail("prof.bus@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        Usuario aluno = new Usuario();
        aluno.setNome("Aluno Bus");
        aluno.setEmail("aluno.bus@edu.com");
        aluno.setSenha("123");
        Usuario alunoSalvo = usuarioRepository.save(aluno);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Busca");
        sala.setCriador(criadorSalvo);
        sala.setUsuarios(java.util.Arrays.asList(alunoSalvo));
        sala.setCodigoSala("BUS11111");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Busca");
        atividade.setDescricao("Buscar submissão");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        // Enviar submissão
        mockMvc.perform(multipart("/api/submissaoatividade/atividade/{atividadeId}/aluno/{alunoId}",
                        atividadeSalva.getId(), alunoSalvo.getId())
                        .param("descricao", "Submissão para busca"))
                .andExpect(status().isOk());

        // Agora buscar
        mockMvc.perform(get("/api/submissaoatividade/atividade/{atividadeId}/aluno/{alunoId}",
                        atividadeSalva.getId(), alunoSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao", is("Submissão para busca")))
                .andExpect(jsonPath("$.aluno.nome", is("Aluno Bus")));
    }

    @Test
    void corrigirSubmissao_DeveAtribuirNotaEFeedback_QuandoValido() throws Exception {
        // Setup: submissão existente
        Usuario criador = new Usuario();
        criador.setNome("Prof Cor");
        criador.setEmail("prof.cor@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        Usuario aluno = new Usuario();
        aluno.setNome("Aluno Cor");
        aluno.setEmail("aluno.cor@edu.com");
        aluno.setSenha("123");
        Usuario alunoSalvo = usuarioRepository.save(aluno);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Correção");
        sala.setCriador(criadorSalvo);
        sala.setUsuarios(java.util.Arrays.asList(alunoSalvo));
        sala.setCodigoSala("COR22222");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Correção");
        atividade.setDescricao("Corrigir trabalho");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        // Criar submissão diretamente no repositório para teste
        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setAtividade(atividadeSalva);
        submissao.setAluno(alunoSalvo);
        submissao.setDescricao("Trabalho a corrigir");
        SubmissaoAtividade salva = submissaoRepository.save(submissao);

        // Corrigir
        mockMvc.perform(put("/api/submissaoatividade/corrigir/{submissaoId}", salva.getId())
                        .param("nota", "8.5")
                        .param("feedback", "Bom trabalho!"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota", is(8.5)))
                .andExpect(jsonPath("$.feedback", is("Bom trabalho!")))
                .andExpect(jsonPath("$.dataCorrecao", notNullValue()));
    }

    @Test
    void marcarComoRecebida_DeveMarcarStatus_QuandoValido() throws Exception {
        // Setup similar ao corrigir
        Usuario criador = new Usuario();
        criador.setNome("Prof Rec");
        criador.setEmail("prof.rec@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        Usuario aluno = new Usuario();
        aluno.setNome("Aluno Rec");
        aluno.setEmail("aluno.rec@edu.com");
        aluno.setSenha("123");
        Usuario alunoSalvo = usuarioRepository.save(aluno);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Recebida");
        sala.setCriador(criadorSalvo);
        sala.setUsuarios(java.util.Arrays.asList(alunoSalvo));
        sala.setCodigoSala("REC33333");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Recebida");
        atividade.setDescricao("Marcar como recebida");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setAtividade(atividadeSalva);
        submissao.setAluno(alunoSalvo);
        submissao.setDescricao("Trabalho recebido");
        SubmissaoAtividade salva = submissaoRepository.save(submissao);

        // Marcar como recebida
        mockMvc.perform(put("/api/submissaoatividade/marcar-recebida/{submissaoId}", salva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recebida", is(true)));
    }

    @Test
    void listarPorAtividade_DeveRetornarSubmissoes_QuandoExistem() throws Exception {
        // Setup: atividade com múltiplas submissões
        Usuario criador = new Usuario();
        criador.setNome("Prof List");
        criador.setEmail("prof.list@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        Usuario aluno1 = new Usuario();
        aluno1.setNome("Aluno1 List");
        aluno1.setEmail("aluno1.list@edu.com");
        aluno1.setSenha("123");
        Usuario aluno1Salvo = usuarioRepository.save(aluno1);

        Usuario aluno2 = new Usuario();
        aluno2.setNome("Aluno2 List");
        aluno2.setEmail("aluno2.list@edu.com");
        aluno2.setSenha("123");
        Usuario aluno2Salvo = usuarioRepository.save(aluno2);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Lista Sub");
        sala.setCriador(criadorSalvo);
        sala.setUsuarios(java.util.Arrays.asList(aluno1Salvo, aluno2Salvo));
        sala.setCodigoSala("LST44444");
        SaladeAula salaSalva = salaRepository.save(sala);

        Atividade atividade = new Atividade();
        atividade.setTitulo("Atividade Lista");
        atividade.setDescricao("Listar submissões");
        atividade.setAutor(criadorSalvo);
        atividade.setSalaDeAula(salaSalva);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        // Criar submissões diretamente
        SubmissaoAtividade sub1 = new SubmissaoAtividade();
        sub1.setAtividade(atividadeSalva);
        sub1.setAluno(aluno1Salvo);
        sub1.setDescricao("Submissão 1");
        submissaoRepository.save(sub1);

        SubmissaoAtividade sub2 = new SubmissaoAtividade();
        sub2.setAtividade(atividadeSalva);
        sub2.setAluno(aluno2Salvo);
        sub2.setDescricao("Submissão 2");
        submissaoRepository.save(sub2);

        mockMvc.perform(get("/api/submissaoatividade/atividade/{atividadeId}", atividadeSalva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].descricao", notNullValue()))
                .andExpect(jsonPath("$[1].descricao", notNullValue()));
    }
}