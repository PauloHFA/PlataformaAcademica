package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SubmissaoAtividadeRespository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissaoAtividadeServiceImpl implements SubmissaoAtividadeService {

    private final SubmissaoAtividadeRespository submissaoRepository;
    private final AtividadeRepository atividadeRepository;
    private final UsuarioRepository usuarioRepository;

    public SubmissaoAtividadeServiceImpl(
            SubmissaoAtividadeRespository submissaoRepository,
            AtividadeRepository atividadeRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.submissaoRepository = submissaoRepository;
        this.atividadeRepository = atividadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ---------------------------------------------------------------
    //  ENVIAR SUBMISSÃO
    // ---------------------------------------------------------------
    @Override
    public SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividade submissao) {

        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SaladeAula sala = atividade.getSalaDeAula();

        // Verifica se o aluno pertence à sala
        boolean membro = sala.getUsuarios().stream()
                .anyMatch(u -> u.getId().equals(alunoId));

        if (!membro) {
            throw new SecurityException("Este usuário não pertence à sala dessa atividade.");
        }

        // Verifica se já existe submissão do aluno
        SubmissaoAtividade existente = submissaoRepository
                .findByAtividadeIdAndAlunoId(atividadeId, alunoId);

        if (existente != null) {
            throw new IllegalStateException("Este aluno já enviou essa atividade.");
        }

        submissao.setAluno(aluno);
        submissao.setAtividade(atividade);
        submissao.setDataSubmissao(LocalDateTime.now());

        return submissaoRepository.save(submissao);
    }

    // ---------------------------------------------------------------
    //  LISTAR SUBMISSÕES POR ATIVIDADE
    // ---------------------------------------------------------------
    @Override
    public List<SubmissaoAtividade> listarSubmissoesPorAtividade(Long atividadeId) {

        atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        return submissaoRepository.findByAtividadeId(atividadeId);
    }

    // ---------------------------------------------------------------
    //  BUSCAR SUBMISSÃO DO ALUNO
    // ---------------------------------------------------------------
    @Override
    public SubmissaoAtividade buscarSubmissaoDoAluno(Long atividadeId, Long alunoId) {

        SubmissaoAtividade submissao =
                submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId);

        if (submissao == null) {
            throw new EntityNotFoundException("Submissão não encontrada para este aluno.");
        }

        return submissao;
    }

    // ---------------------------------------------------------------
    //  CORRIGIR SUBMISSÃO (professor atribui nota e feedback)
    // ---------------------------------------------------------------
    @Override
    public SubmissaoAtividade corrigirSubmissao(Long submissaoId, Double nota, String feedback) {

        SubmissaoAtividade submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new EntityNotFoundException("Submissão não encontrada."));

        submissao.setNota(nota);
        submissao.setFeedback(feedback);
        submissao.setDataCorrecao(LocalDateTime.now());

        return submissaoRepository.save(submissao);
    }
}
