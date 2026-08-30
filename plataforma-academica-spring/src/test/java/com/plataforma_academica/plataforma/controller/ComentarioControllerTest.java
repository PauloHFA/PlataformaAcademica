package com.plataforma_academica.plataforma.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.PostagemRepository;
import com.plataforma_academica.plataforma.service.ComentarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComentarioController.class)
class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioService comentarioService;

    @MockBean
    private SaladeAulaRepository salaRepository;

    @MockBean
    private AtividadeRepository atividadeRepository;

    @MockBean
    private PostagemRepository postagemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarTodos_DeveRetornarListaDeComentarios_QuandoChamado() throws Exception {
        // Arrange
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario1 = new Comentario();
        comentario1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario1.setConteudo("Comentário 1");
        comentario1.setAutor(autor);

        Comentario comentario2 = new Comentario();
        comentario2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        comentario2.setConteudo("Comentário 2");
        comentario2.setAutor(autor);

        List<Comentario> comentarios = List.of(comentario1, comentario2);

        when(comentarioService.listarTodos()).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/comentario"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].conteudo").value("Comentário 1"))
                .andExpect(jsonPath("$[1].id").value(UUID.fromString("00000000-0000-0000-0000-000000000002")))
                .andExpect(jsonPath("$[1].conteudo").value("Comentário 2"));
    }

    @Test
    void buscarPorId_DeveRetornarComentario_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario = new Comentario();
        comentario.setId(id);
        comentario.setConteudo("Comentário teste");
        comentario.setAutor(autor);

        when(comentarioService.buscarPorId(id)).thenReturn(comentario);

        // Act & Assert
        mockMvc.perform(get("/comentario/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.conteudo").value("Comentário teste"));
    }

    @Test
    void buscarPorId_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(comentarioService.buscarPorId(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/comentario/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void salvar_DeveRetornarComentarioCriado_QuandoValido() throws Exception {
        // Arrange
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        SaladeAula sala = new SaladeAula();
        sala.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        sala.setNome("Sala de Matemática");

        Comentario request = new Comentario();
        request.setConteudo("Novo comentário");
        request.setTipoDestino(TipoDestinoComentario.SALADEAULA);
        request.setAutor(autor);
        request.setSaladeAula(sala);

        Comentario comentarioSalvo = new Comentario();
        comentarioSalvo.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentarioSalvo.setConteudo("Novo comentário");
        comentarioSalvo.setTipoDestino(TipoDestinoComentario.SALADEAULA);
        comentarioSalvo.setAutor(autor);
        comentarioSalvo.setSaladeAula(sala);

        when(salaRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(sala));
        when(comentarioService.salvar(any(Comentario.class))).thenReturn(comentarioSalvo);

        // Act & Assert
        mockMvc.perform(post("/comentario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/comentario/1"))
                .andExpect(jsonPath("$.id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$.conteudo").value("Novo comentário"));
    }

    @Test
    void atualizar_DeveRetornarComentarioAtualizado_QuandoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentarioAtualizado = new Comentario();
        comentarioAtualizado.setId(id);
        comentarioAtualizado.setConteudo("Comentário atualizado");
        comentarioAtualizado.setAutor(autor);

        when(comentarioService.atualizar(anyLong(), any(Comentario.class))).thenReturn(comentarioAtualizado);

        // Act & Assert
        mockMvc.perform(put("/comentario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.conteudo").value("Comentário atualizado"));
    }

    @Test
    void atualizar_DeveRetornarNotFound_QuandoNaoEncontrado() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Comentario comentario = new Comentario();
        comentario.setConteudo("Conteúdo");

        when(comentarioService.atualizar(anyLong(), any(Comentario.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/comentario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentario)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_DeveRetornarNoContent_QuandoComentarioExiste() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        Comentario comentario = new Comentario();
        comentario.setId(id);
        comentario.setAutor(autor);

        when(comentarioService.buscarPorId(id)).thenReturn(comentario);

        // Act & Assert
        mockMvc.perform(delete("/comentario/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_DeveRetornarNotFound_QuandoComentarioNaoExiste() throws Exception {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        when(comentarioService.buscarPorId(id)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/comentario/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarComentariosSala_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario = new Comentario();
        comentario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setConteudo("Comentário da sala");
        comentario.setTipoDestino(TipoDestinoComentario.SALADEAULA);
        comentario.setAutor(autor);

        List<Comentario> comentarios = List.of(comentario);

        when(comentarioService.listarComentariosPorSala(salaId)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/comentario/sala/{salaId}", salaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].conteudo").value("Comentário da sala"));
    }

    @Test
    void listarComentariosAtividadesGerais_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long salaId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario = new Comentario();
        comentario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setConteudo("Comentário geral");
        comentario.setTipoDestino(TipoDestinoComentario.ATIVIDADES_GERAIS);
        comentario.setAutor(autor);

        List<Comentario> comentarios = List.of(comentario);

        when(comentarioService.listarComentariosPorSala(salaId)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/comentario/sala/{salaId}/atividades-gerais", salaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].conteudo").value("Comentário geral"));
    }

    @Test
    void listarComentariosAtividade_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long atividadeId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario = new Comentario();
        comentario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setConteudo("Comentário da atividade");
        comentario.setAutor(autor);

        List<Comentario> comentarios = List.of(comentario);

        when(comentarioService.listarComentariosPorAtividade(atividadeId)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/comentario/atividade/{atividadeId}", atividadeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].conteudo").value("Comentário da atividade"));
    }

    @Test
    void listarComentariosPostagem_DeveRetornarLista_QuandoChamado() throws Exception {
        // Arrange
        Long postagemId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Usuario autor = new Usuario();
        autor.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        autor.setNome("João Silva");

        Comentario comentario = new Comentario();
        comentario.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        comentario.setConteudo("Comentário da postagem");
        comentario.setAutor(autor);

        List<Comentario> comentarios = List.of(comentario);

        when(comentarioService.listarComentariosPorPostagem(postagemId)).thenReturn(comentarios);

        // Act & Assert
        mockMvc.perform(get("/comentario/postagem/{postagemId}", postagemId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(UUID.fromString("00000000-0000-0000-0000-000000000001")))
                .andExpect(jsonPath("$[0].conteudo").value("Comentário da postagem"));
    }
}