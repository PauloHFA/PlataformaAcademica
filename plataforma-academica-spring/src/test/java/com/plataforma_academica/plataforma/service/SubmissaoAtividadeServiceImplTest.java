package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SubmissaoAtividadeRespository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissaoAtividadeServiceImplTest {

    @Mock
    private SubmissaoAtividadeRespository submissaoRepository;

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private SubmissaoAtividadeServiceImpl submissaoService;

    @Test
    void enviarSubmissao_ComSubmissaoObjeto_DeveRetornarSubmissao_QuandoValido() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);
        Usuario autor = new Usuario();
        autor.setId(2L);
        atividade.setAutor(autor);

        Usuario usuarioInSala = new Usuario();
        usuarioInSala.setId(alunoId);
        SaladeAula sala = new SaladeAula();
        sala.setUsuarios(List.of(usuarioInSala));

        atividade.setSalaDeAula(sala);

        Usuario aluno = new Usuario();
        aluno.setId(alunoId);
        aluno.setNome("Nome");

        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setDescricao("Descrição");

        SubmissaoAtividade saved = new SubmissaoAtividade();
        saved.setId(3L);

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(atividade));
        when(usuarioRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        when(submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId)).thenReturn(null);
        when(submissaoRepository.save(any(SubmissaoAtividade.class))).thenReturn(saved);

        // Act
        SubmissaoAtividade result = submissaoService.enviarSubmissao(atividadeId, alunoId, submissao);

        // Assert
        assertNotNull(result);
        assertEquals(saved, result);
        verify(submissaoRepository).save(any(SubmissaoAtividade.class));
        verify(notificacaoService).criarNotificacao(anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    void enviarSubmissao_ComSubmissaoObjeto_DeveLancarEntityNotFoundException_QuandoAtividadeNaoEncontrada() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        SubmissaoAtividade submissao = new SubmissaoAtividade();

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> submissaoService.enviarSubmissao(atividadeId, alunoId, submissao));
        assertEquals("Atividade não encontrada.", exception.getMessage());
    }

    @Test
    void enviarSubmissao_ComSubmissaoObjeto_DeveLancarSecurityException_QuandoNaoMembro() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);
        SaladeAula sala = new SaladeAula();
        sala.setUsuarios(List.of()); // Empty list
        atividade.setSalaDeAula(sala);

        Usuario aluno = new Usuario();
        aluno.setId(alunoId);

        SubmissaoAtividade submissao = new SubmissaoAtividade();

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(atividade));
        when(usuarioRepository.findById(alunoId)).thenReturn(Optional.of(aluno));

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> submissaoService.enviarSubmissao(atividadeId, alunoId, submissao));
        assertEquals("Este usuário não pertence à sala dessa atividade.", exception.getMessage());
    }

    @Test
    void enviarSubmissao_ComSubmissaoObjeto_DeveLancarIllegalStateException_QuandoJaEnviada() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);
        SaladeAula sala = new SaladeAula();
        Usuario usuarioInSala2 = new Usuario();
        usuarioInSala2.setId(alunoId);
        sala.setUsuarios(List.of(usuarioInSala2));
        atividade.setSalaDeAula(sala);

        Usuario aluno = new Usuario();
        aluno.setId(alunoId);

        SubmissaoAtividade existente = new SubmissaoAtividade();
        SubmissaoAtividade submissao = new SubmissaoAtividade();

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(atividade));
        when(usuarioRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        when(submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId)).thenReturn(existente);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> submissaoService.enviarSubmissao(atividadeId, alunoId, submissao));
        assertEquals("Este aluno já enviou essa atividade.", exception.getMessage());
    }

    @Test
    void listarSubmissoesPorAtividade_DeveRetornarLista_QuandoValido() {
        // Arrange
        Long atividadeId = 1L;

        Atividade atividade = new Atividade();
        atividade.setId(atividadeId);

        List<SubmissaoAtividade> submissoes = List.of(new SubmissaoAtividade());

        when(atividadeRepository.findById(atividadeId)).thenReturn(Optional.of(atividade));
        when(submissaoRepository.findByAtividadeId(atividadeId)).thenReturn(submissoes);

        // Act
        List<SubmissaoAtividade> result = submissaoService.listarSubmissoesPorAtividade(atividadeId);

        // Assert
        assertEquals(submissoes, result);
        verify(submissaoRepository).findByAtividadeId(atividadeId);
    }

    @Test
    void buscarSubmissaoDoAluno_DeveRetornarSubmissao_QuandoEncontrada() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        SubmissaoAtividade submissao = new SubmissaoAtividade();

        when(submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId)).thenReturn(submissao);

        // Act
        SubmissaoAtividade result = submissaoService.buscarSubmissaoDoAluno(atividadeId, alunoId);

        // Assert
        assertEquals(submissao, result);
    }

    @Test
    void buscarSubmissaoDoAluno_DeveLancarEntityNotFoundException_QuandoNaoEncontrada() {
        // Arrange
        Long atividadeId = 1L;
        Long alunoId = 1L;

        when(submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId)).thenReturn(null);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> submissaoService.buscarSubmissaoDoAluno(atividadeId, alunoId));
        assertEquals("Submissão não encontrada para este aluno.", exception.getMessage());
    }

    @Test
    void corrigirSubmissao_DeveRetornarSubmissao_QuandoValido() {
        // Arrange
        Long submissaoId = 1L;
        Double nota = 9.5;
        String feedback = "Bom trabalho";

        Atividade atividade = new Atividade();
        atividade.setTitulo("Título");

        Usuario aluno = new Usuario();
        aluno.setId(2L);

        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setId(submissaoId);
        submissao.setAtividade(atividade);
        submissao.setAluno(aluno);

        SubmissaoAtividade saved = new SubmissaoAtividade();
        saved.setId(submissaoId);
        saved.setNota(nota);
        saved.setFeedback(feedback);

        when(submissaoRepository.findById(submissaoId)).thenReturn(Optional.of(submissao));
        when(submissaoRepository.save(submissao)).thenReturn(saved);

        // Act
        SubmissaoAtividade result = submissaoService.corrigirSubmissao(submissaoId, nota, feedback);

        // Assert
        assertEquals(saved, result);
        assertEquals(nota, result.getNota());
        assertEquals(feedback, result.getFeedback());
        verify(notificacaoService).criarNotificacao(anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    void marcarComoRecebida_DeveRetornarSubmissao_QuandoValido() {
        // Arrange
        Long submissaoId = 1L;

        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setId(submissaoId);
        submissao.setRecebida(false);

        SubmissaoAtividade saved = new SubmissaoAtividade();
        saved.setId(submissaoId);
        saved.setRecebida(true);

        when(submissaoRepository.findById(submissaoId)).thenReturn(Optional.of(submissao));
        when(submissaoRepository.save(submissao)).thenReturn(saved);

        // Act
        SubmissaoAtividade result = submissaoService.marcarComoRecebida(submissaoId);

        // Assert
        assertTrue(result.getRecebida());
        verify(submissaoRepository).save(submissao);
    }
}