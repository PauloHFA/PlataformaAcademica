package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.SubmissaoAtividadeMapper;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardAlunoServiceImpl implements DashboardAlunoService {

    private final SubmissaoAtividadeService submissaoAtividadeService;
    private final FrequenciaService frequenciaService;
    private final AtividadeRepository atividadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final SaladeAulaRepository saladeAulaRepository;

    public DashboardAlunoServiceImpl(
            SubmissaoAtividadeService submissaoAtividadeService,
            FrequenciaService frequenciaService,
            AtividadeRepository atividadeRepository,
            UsuarioRepository usuarioRepository,
            SaladeAulaRepository saladeAulaRepository
    ) {
        this.submissaoAtividadeService = submissaoAtividadeService;
        this.frequenciaService = frequenciaService;
        this.atividadeRepository = atividadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.saladeAulaRepository = saladeAulaRepository;
    }

    @Override
    public DashboardAlunoDTO obterDashboardAluno(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim) {
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SaladeAula sala = saladeAulaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));

        List<com.plataforma_academica.plataforma.model.Atividade> atividades = atividadeRepository.findBySalaDeAulaId(salaId);
        List<com.plataforma_academica.plataforma.model.SubmissaoAtividade> submissoes =
                submissaoAtividadeService.listarSubmissoesPorAlunoESala(alunoId, salaId);

        List<SubmissaoAtividadeResponseDTO> submissoesDTO = submissoes.stream()
                .map(SubmissaoAtividadeMapper::toResponse)
                .collect(Collectors.toList());

        long totalSubmissoesComNota = submissoes.stream()
                .filter(s -> s.getNota() != null)
                .count();

        double mediaNota = submissoes.stream()
                .filter(s -> s.getNota() != null)
                .mapToDouble(com.plataforma_academica.plataforma.model.SubmissaoAtividade::getNota)
                .average()
                .orElse(0.0);

        List<com.plataforma_academica.plataforma.model.Frequencia> frequencias;
        if (inicio != null && fim != null) {
            frequencias = frequenciaService.buscarFrequencias(alunoId, salaId, inicio, fim);
        } else {
            frequencias = frequenciaService.buscarFrequencias(alunoId, salaId);
        }

        long totalPresencas = frequencias.stream().filter(com.plataforma_academica.plataforma.model.Frequencia::getPresente).count();
        long totalFaltas = frequencias.stream().filter(f -> !f.getPresente()).count();

        double percentualPresenca;
        if (inicio != null && fim != null) {
            percentualPresenca = frequenciaService.calcularPercentualPresenca(alunoId, salaId, inicio, fim);
        } else {
            percentualPresenca = !frequencias.isEmpty() ? (totalPresencas * 100.0) / frequencias.size() : 0.0;
        }

        DashboardAlunoDTO dto = new DashboardAlunoDTO();
        dto.setAlunoId(aluno.getId());
        dto.setAlunoNome(aluno.getNome());
        dto.setSalaId(sala.getId());
        dto.setSalaNome(sala.getNome());
        dto.setTotalAtividades(atividades.size());
        dto.setTotalSubmissoes(submissoes.size());
        dto.setTotalSubmissoesComNota(Math.toIntExact(totalSubmissoesComNota));
        dto.setMediaNota(mediaNota);
        dto.setTotalPresencas(Math.toIntExact(totalPresencas));
        dto.setTotalFaltas(Math.toIntExact(totalFaltas));
        dto.setPercentualPresenca(percentualPresenca);
        dto.setSubmissoes(submissoesDTO);

        return dto;
    }
}
