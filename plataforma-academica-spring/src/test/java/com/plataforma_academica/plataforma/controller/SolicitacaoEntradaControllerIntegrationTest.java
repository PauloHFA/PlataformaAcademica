package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SolicitacaoEntrada;
import com.plataforma_academica.plataforma.model.SolicitacaoEntrada.StatusSolicitacao;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.SolicitacaoEntradaRepository;
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
 * Teste de Integração para SolicitacaoEntradaController
 *
 * Usa H2 em memória e contexto Spring completo.
 * Perfeito para testar controllers com dependências reais.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Garante rollback após cada teste
class SolicitacaoEntradaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SolicitacaoEntradaRepository solicitacaoRepository;

    @Autowired
    private SaladeAulaRepository salaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void solicitarEntrada_DeveCriarSolicitacao_QuandoValida() throws Exception {
        // Setup: criar sala e usuário
        Usuario criador = new Usuario();
        criador.setNome("Prof Solic");
        criador.setEmail("prof.solic@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Solicitações");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("SOL12345");
        SaladeAula salaSalva = salaRepository.save(sala);

        Usuario solicitante = new Usuario();
        solicitante.setNome("Aluno Solicitante");
        solicitante.setEmail("aluno.solic@edu.com");
        solicitante.setSenha("123");
        Usuario solicitanteSalvo = usuarioRepository.save(solicitante);

        mockMvc.perform(post("/api/solicitacoes/solicitar/{salaId}/{usuarioId}",
                        salaSalva.getId(), solicitanteSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PENDENTE")))
                .andExpect(jsonPath("$.sala.nome", is("Sala Solicitações")))
                .andExpect(jsonPath("$.usuario.nome", is("Aluno Solicitante")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void solicitarEntrada_DeveRetornarBadRequest_QuandoJaExistePendente() throws Exception {
        // Setup: criar sala, usuário e solicitação pendente
        Usuario criador = new Usuario();
        criador.setNome("Prof Dup");
        criador.setEmail("prof.dup@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Duplicada");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("DUP67890");
        SaladeAula salaSalva = salaRepository.save(sala);

        Usuario solicitante = new Usuario();
        solicitante.setNome("Aluno Dup");
        solicitante.setEmail("aluno.dup@edu.com");
        solicitante.setSenha("123");
        Usuario solicitanteSalvo = usuarioRepository.save(solicitante);

        // Criar solicitação pendente
        SolicitacaoEntrada solicitacao = new SolicitacaoEntrada();
        solicitacao.setSala(salaSalva);
        solicitacao.setUsuario(solicitanteSalvo);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacaoRepository.save(solicitacao);

        // Tentar solicitar novamente
        mockMvc.perform(post("/api/solicitacoes/solicitar/{salaId}/{usuarioId}",
                        salaSalva.getId(), solicitanteSalvo.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Já existe uma solicitação pendente"));
    }

    @Test
    void listarPendentes_DeveRetornarPendentes_QuandoExistem() throws Exception {
        // Setup: sala e solicitações
        Usuario criador = new Usuario();
        criador.setNome("Prof List");
        criador.setEmail("prof.list@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Lista");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("LST11111");
        SaladeAula salaSalva = salaRepository.save(sala);

        Usuario aluno1 = new Usuario();
        aluno1.setNome("Aluno1");
        aluno1.setEmail("aluno1@edu.com");
        aluno1.setSenha("123");
        Usuario aluno1Salvo = usuarioRepository.save(aluno1);

        Usuario aluno2 = new Usuario();
        aluno2.setNome("Aluno2");
        aluno2.setEmail("aluno2@edu.com");
        aluno2.setSenha("123");
        Usuario aluno2Salvo = usuarioRepository.save(aluno2);

        // Criar solicitações pendentes
        SolicitacaoEntrada sol1 = new SolicitacaoEntrada();
        sol1.setSala(salaSalva);
        sol1.setUsuario(aluno1Salvo);
        sol1.setStatus(StatusSolicitacao.PENDENTE);
        solicitacaoRepository.save(sol1);

        SolicitacaoEntrada sol2 = new SolicitacaoEntrada();
        sol2.setSala(salaSalva);
        sol2.setUsuario(aluno2Salvo);
        sol2.setStatus(StatusSolicitacao.PENDENTE);
        solicitacaoRepository.save(sol2);

        mockMvc.perform(get("/api/solicitacoes/sala/{salaId}/pendentes", salaSalva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status", is("PENDENTE")))
                .andExpect(jsonPath("$[1].status", is("PENDENTE")));
    }

    @Test
    void aprovar_DeveAprovarSolicitacao_QuandoCriadorAutoriza() throws Exception {
        // Setup: sala, criador, solicitante e solicitação
        Usuario criador = new Usuario();
        criador.setNome("Prof Aprov");
        criador.setEmail("prof.aprov@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Aprovação");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("APR22222");
        SaladeAula salaSalva = salaRepository.save(sala);

        Usuario solicitante = new Usuario();
        solicitante.setNome("Aluno Aprov");
        solicitante.setEmail("aluno.aprov@edu.com");
        solicitante.setSenha("123");
        Usuario solicitanteSalvo = usuarioRepository.save(solicitante);

        SolicitacaoEntrada solicitacao = new SolicitacaoEntrada();
        solicitacao.setSala(salaSalva);
        solicitacao.setUsuario(solicitanteSalvo);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        SolicitacaoEntrada salva = solicitacaoRepository.save(solicitacao);

        // Aprovar
        mockMvc.perform(put("/api/solicitacoes/{solicitacaoId}/aprovar/{professorId}",
                        salva.getId(), criadorSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APROVADA")))
                .andExpect(jsonPath("$.dataResposta", notNullValue()));
    }

    @Test
    void rejeitar_DeveRejeitarSolicitacao_QuandoCriadorAutoriza() throws Exception {
        // Setup: similar ao aprovar
        Usuario criador = new Usuario();
        criador.setNome("Prof Rej");
        criador.setEmail("prof.rej@edu.com");
        criador.setSenha("123");
        Usuario criadorSalvo = usuarioRepository.save(criador);

        SaladeAula sala = new SaladeAula();
        sala.setNome("Sala Rejeição");
        sala.setCriador(criadorSalvo);
        sala.setCodigoSala("REJ33333");
        SaladeAula salaSalva = salaRepository.save(sala);

        Usuario solicitante = new Usuario();
        solicitante.setNome("Aluno Rej");
        solicitante.setEmail("aluno.rej@edu.com");
        solicitante.setSenha("123");
        Usuario solicitanteSalvo = usuarioRepository.save(solicitante);

        SolicitacaoEntrada solicitacao = new SolicitacaoEntrada();
        solicitacao.setSala(salaSalva);
        solicitacao.setUsuario(solicitanteSalvo);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        SolicitacaoEntrada salva = solicitacaoRepository.save(solicitacao);

        // Rejeitar
        mockMvc.perform(put("/api/solicitacoes/{solicitacaoId}/rejeitar/{professorId}",
                        salva.getId(), criadorSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("REJEITADA")))
                .andExpect(jsonPath("$.dataResposta", notNullValue()));
    }

    @Test
    void minhasSolicitacoes_DeveRetornarSolicitacoesPendentes_DoUsuario() throws Exception {
        // Setup: usuário e suas solicitações
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Minhas");
        usuario.setEmail("usuario.minhas@edu.com");
        usuario.setSenha("123");
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Criar duas salas com criadores
        Usuario criador1 = new Usuario();
        criador1.setNome("Criador1");
        criador1.setEmail("criador1@edu.com");
        criador1.setSenha("123");
        Usuario criador1Salvo = usuarioRepository.save(criador1);

        SaladeAula sala1 = new SaladeAula();
        sala1.setNome("Sala1");
        sala1.setCriador(criador1Salvo);
        sala1.setCodigoSala("SAL44444");
        SaladeAula sala1Salva = salaRepository.save(sala1);

        // Solicitação pendente
        SolicitacaoEntrada solPendente = new SolicitacaoEntrada();
        solPendente.setSala(sala1Salva);
        solPendente.setUsuario(usuarioSalvo);
        solPendente.setStatus(StatusSolicitacao.PENDENTE);
        solicitacaoRepository.save(solPendente);

        // Solicitação aprovada (não deve aparecer)
        SolicitacaoEntrada solAprovada = new SolicitacaoEntrada();
        solAprovada.setSala(sala1Salva);
        solAprovada.setUsuario(usuarioSalvo);
        solAprovada.setStatus(StatusSolicitacao.APROVADA);
        solicitacaoRepository.save(solAprovada);

        mockMvc.perform(get("/api/solicitacoes/usuario/{usuarioId}/minhas", usuarioSalvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("PENDENTE")));
    }
}