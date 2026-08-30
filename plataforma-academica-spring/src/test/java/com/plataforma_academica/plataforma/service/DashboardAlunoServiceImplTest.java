package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.DashboardSalaDTO;
import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.SubmissaoAtividadeMapper;
import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardAlunoServiceImplTest {

    @Mock
    private SubmissaoAtividadeService submissaoAtividadeService;

    @Mock
    private FrequenciaService frequenciaService;

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SaladeAulaRepository saladeAulaRepository;

    @InjectMocks
    private DashboardAlunoServiceImpl dashboardAlunoService;

    private Usuario aluno;
    private SaladeAula sala;
    private Atividade atividade;
    private SubmissaoAtividade submissao;
    private Frequencia frequencia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        aluno = new Usuario();
        aluno.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        aluno.setNome("João Silva");

        sala = new SaladeAula();
        sala.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        sala.setNome("Sala de POO");

        atividade = new Atividade();
        atividade.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        atividade.setSalaDeAula(sala);

        submissao = new SubmissaoAtividade();
        submissao.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        submissao.setNota(8.5);
        submissao.setAtividade(atividade);

        frequencia = new Frequencia();
        frequencia.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        frequencia.setPresente(true);
    }

    @Test
    void obterDashboardAluno_DeveRetornarDashboardCorreto() {
        // Arrange
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(aluno));
        when(saladeAulaRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(sala));
        when(atividadeRepository.findBySalaDeAulaId(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(atividade));
        when(submissaoAtividadeService.listarSubmissoesPorAlunoESala(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(submissao));
        when(frequenciaService.buscarFrequencias(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(frequencia));

        // Act
        DashboardAlunoDTO result = dashboardAlunoService.obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);

        // Assert
        assertNotNull(result);
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result.getAlunoId());
        assertEquals("João Silva", result.getAlunoNome());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result.getSalaId());
        assertEquals("Sala de POO", result.getSalaNome());
        assertEquals(1, result.getTotalAtividades());
        assertEquals(1, result.getTotalSubmissoes());
        assertEquals(1, result.getTotalSubmissoesComNota());
        assertEquals(8.5, result.getMediaNota());
        assertEquals(1, result.getTotalPresencas());
        assertEquals(0, result.getTotalFaltas());
        assertEquals(100.0, result.getPercentualPresenca());
        assertEquals(1, result.getSubmissoes().size());
    }

    @Test
    void obterDashboardAluno_AlunoNaoEncontrado_DeveLancarExcecao() {
        // Arrange
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dashboardAlunoService.obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null));
    }

    @Test
    void obterDashboardAluno_SalaNaoEncontrada_DeveLancarExcecao() {
        // Arrange
        when(usuarioRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(aluno));
        when(saladeAulaRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dashboardAlunoService.obterDashboardAluno(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null));
    }

    @Test
    void obterDashboardSala_DeveRetornarResumoSalaCorreto() {
        // Arrange
        sala.setUsuarios(Arrays.asList(aluno));

        when(saladeAulaRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(sala));
        when(atividadeRepository.findBySalaDeAulaId(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(atividade));
        when(submissaoAtividadeService.listarSubmissoesPorAlunoESala(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(submissao));
        when(frequenciaService.buscarFrequencias(UUID.fromString("00000000-0000-0000-0000-000000000001"), UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Arrays.asList(frequencia));

        // Act
        DashboardSalaDTO result = dashboardAlunoService.obterDashboardSala(UUID.fromString("00000000-0000-0000-0000-000000000001"), null, null);

        // Assert
        assertNotNull(result);
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result.getSalaId());
        assertEquals("Sala de POO", result.getSalaNome());
        assertEquals(1, result.getTotalAtividades());
        assertEquals(1, result.getTotalSubmissoes());
        assertEquals(1, result.getTotalSubmissoesComNota());
        assertEquals(8.5, result.getMediaNotaSala());
        assertEquals(1, result.getTotalPresencas());
        assertEquals(0, result.getTotalFaltas());
        assertEquals(100.0, result.getPercentualPresenca());
        assertNotNull(result.getAlunos());
        assertEquals(1, result.getAlunos().size());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), result.getAlunos().get(0).getAlunoId());
        assertEquals("João Silva", result.getAlunos().get(0).getAlunoNome());
        assertEquals(1, result.getAlunos().get(0).getTotalSubmissoes());
        assertEquals(1, result.getAlunos().get(0).getTotalSubmissoesComNota());
        assertEquals(8.5, result.getAlunos().get(0).getMediaNota());
        assertEquals(100.0, result.getAlunos().get(0).getPercentualPresenca());
    }
}