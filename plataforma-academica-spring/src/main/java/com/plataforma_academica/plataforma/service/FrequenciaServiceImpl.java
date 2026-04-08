package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Frequencia;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.FrequenciaRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FrequenciaServiceImpl implements FrequenciaService {

    private final FrequenciaRepository frequenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SaladeAulaRepository salaRepository;

    public FrequenciaServiceImpl(FrequenciaRepository frequenciaRepository,
                                 UsuarioRepository usuarioRepository,
                                 SaladeAulaRepository salaRepository) {
        this.frequenciaRepository = frequenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
    }

    @Override
    public Frequencia registrarFrequencia(Long alunoId, Long salaId, LocalDate data, Boolean presente, String justificativa) {
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SaladeAula sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada."));

        Frequencia frequencia = new Frequencia();
        frequencia.setAluno(aluno);
        frequencia.setSalaDeAula(sala);
        frequencia.setData(data);
        frequencia.setPresente(presente);
        frequencia.setJustificativa(justificativa);

        return frequenciaRepository.save(frequencia);
    }

    @Override
    public List<Frequencia> buscarFrequencias(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            return buscarFrequencias(alunoId, salaId);
        }
        return frequenciaRepository.findByAlunoIdAndSalaDeAulaIdAndDataBetween(alunoId, salaId, inicio, fim);
    }

    @Override
    public List<Frequencia> buscarFrequencias(Long alunoId, Long salaId) {
        return frequenciaRepository.findByAlunoIdAndSalaDeAulaId(alunoId, salaId);
    }

    @Override
    public double calcularPercentualPresenca(Long alunoId, Long salaId, LocalDate inicio, LocalDate fim) {
        List<Frequencia> lista = buscarFrequencias(alunoId, salaId, inicio, fim);

        if (lista.isEmpty()) return 0.0;

        long presentes = lista.stream().filter(Frequencia::getPresente).count();
        return 100.0 * presentes / lista.size();
    }
}
