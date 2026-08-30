package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AlunoDashboardResumoDTO;
import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.dto.DashboardSalaDTO;
import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeResponseDTO;
import com.plataforma_academica.plataforma.mapper.SubmissaoAtividadeMapper;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Dashboard do Aluno.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern, Aggregator Pattern.
 * 
 * @see DashboardAlunoService
 * @see docs/domain/academic_context.md
 * @see REQ-030 (Dashboard Acadêmico)
 */
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
                        SaladeAulaRepository saladeAulaRepository) {
                this.submissaoAtividadeService = submissaoAtividadeService;
                this.frequenciaService = frequenciaService;
                this.atividadeRepository = atividadeRepository;
                this.usuarioRepository = usuarioRepository;
                this.saladeAulaRepository = saladeAulaRepository;
        }

        @Override
        public DashboardAlunoDTO obterDashboardAluno(UUID alunoId, UUID salaId, LocalDate inicio, LocalDate fim) {
                Usuario aluno = usuarioRepository.findById(alunoId)
                                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

                SaladeAula sala = saladeAulaRepository.findById(salaId)
                                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));

                List<com.plataforma_academica.plataforma.model.Atividade> atividades = atividadeRepository
                                .findBySalaDeAulaId(salaId);
                List<com.plataforma_academica.plataforma.model.SubmissaoAtividade> submissoes = submissaoAtividadeService
                                .listarSubmissoesPorAlunoESala(alunoId, salaId);

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

                long totalPresencas = frequencias.stream()
                                .filter(com.plataforma_academica.plataforma.model.Frequencia::getPresente).count();
                long totalFaltas = frequencias.stream().filter(f -> !f.getPresente()).count();

                double percentualPresenca;
                if (inicio != null && fim != null) {
                        percentualPresenca = frequenciaService.calcularPercentualPresenca(alunoId, salaId, inicio, fim);
                } else {
                        percentualPresenca = !frequencias.isEmpty() ? (totalPresencas * 100.0) / frequencias.size()
                                        : 0.0;
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

        @Override
        public DashboardSalaDTO obterDashboardSala(UUID salaId, LocalDate inicio, LocalDate fim) {
                SaladeAula sala = saladeAulaRepository.findById(salaId)
                                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));

                List<com.plataforma_academica.plataforma.model.Atividade> atividades = atividadeRepository
                                .findBySalaDeAulaId(salaId);
                List<Usuario> alunos = sala.getUsuarios() != null ? sala.getUsuarios() : Collections.emptyList();

                int totalSubmissoesSala = 0;
                int totalSubmissoesComNotaSala = 0;
                double somaNotasSala = 0.0;
                int quantidadeNotasSala = 0;
                int totalPresencasSala = 0;
                int totalFaltasSala = 0;

                List<AlunoDashboardResumoDTO> alunosResumo = new ArrayList<>();

                for (Usuario aluno : alunos) {
                        if (aluno == null || aluno.getId() == null) {
                                continue;
                        }

                        List<SubmissaoAtividade> submissoesAluno = submissaoAtividadeService
                                        .listarSubmissoesPorAlunoESala(aluno.getId(), salaId);
                        List<com.plataforma_academica.plataforma.model.Frequencia> frequenciasAluno;
                        if (inicio != null && fim != null) {
                                frequenciasAluno = frequenciaService.buscarFrequencias(aluno.getId(), salaId, inicio,
                                                fim);
                        } else {
                                frequenciasAluno = frequenciaService.buscarFrequencias(aluno.getId(), salaId);
                        }

                        long totalSubmissoesAluno = submissoesAluno.size();
                        long totalSubmissoesComNotaAluno = submissoesAluno.stream()
                                        .filter(s -> s.getNota() != null)
                                        .count();
                        double mediaNotaAluno = submissoesAluno.stream()
                                        .filter(s -> s.getNota() != null)
                                        .mapToDouble(SubmissaoAtividade::getNota)
                                        .average()
                                        .orElse(0.0);

                        int presencasAluno = (int) frequenciasAluno.stream()
                                        .filter(com.plataforma_academica.plataforma.model.Frequencia::getPresente)
                                        .count();
                        int faltasAluno = (int) frequenciasAluno.stream().filter(f -> !f.getPresente()).count();
                        double percentualPresencaAluno = !frequenciasAluno.isEmpty()
                                        ? (presencasAluno * 100.0) / frequenciasAluno.size()
                                        : 0.0;

                        totalSubmissoesSala += totalSubmissoesAluno;
                        totalSubmissoesComNotaSala += totalSubmissoesComNotaAluno;
                        somaNotasSala += submissoesAluno.stream()
                                        .filter(s -> s.getNota() != null)
                                        .mapToDouble(SubmissaoAtividade::getNota)
                                        .sum();
                        quantidadeNotasSala += (int) totalSubmissoesComNotaAluno;
                        totalPresencasSala += presencasAluno;
                        totalFaltasSala += faltasAluno;

                        AlunoDashboardResumoDTO alunoResumo = new AlunoDashboardResumoDTO();
                        alunoResumo.setAlunoId(aluno.getId());
                        alunoResumo.setAlunoNome(aluno.getNome());
                        alunoResumo.setTotalSubmissoes((int) totalSubmissoesAluno);
                        alunoResumo.setTotalSubmissoesComNota((int) totalSubmissoesComNotaAluno);
                        alunoResumo.setMediaNota(mediaNotaAluno);
                        alunoResumo.setPercentualPresenca(percentualPresencaAluno);
                        alunosResumo.add(alunoResumo);
                }

                double mediaNotaSala = quantidadeNotasSala > 0 ? somaNotasSala / quantidadeNotasSala : 0.0;
                double percentualPresencaSala = (totalPresencasSala + totalFaltasSala) > 0
                                ? (totalPresencasSala * 100.0) / (totalPresencasSala + totalFaltasSala)
                                : 0.0;

                DashboardSalaDTO salaDTO = new DashboardSalaDTO();
                salaDTO.setSalaId(sala.getId());
                salaDTO.setSalaNome(sala.getNome());
                salaDTO.setTotalAtividades(atividades.size());
                salaDTO.setTotalSubmissoes(totalSubmissoesSala);
                salaDTO.setTotalSubmissoesComNota(totalSubmissoesComNotaSala);
                salaDTO.setMediaNotaSala(mediaNotaSala);
                salaDTO.setTotalPresencas(totalPresencasSala);
                salaDTO.setTotalFaltas(totalFaltasSala);
                salaDTO.setPercentualPresenca(percentualPresencaSala);
                salaDTO.setAlunos(alunosResumo);

                return salaDTO;
        }
}
