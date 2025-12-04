package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.SubmissaoAtividadeDTO;
import com.plataforma_academica.plataforma.mapper.SubmissaoAtividadeMapper;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.SubmissaoAtividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SubmissaoAtividadeRespository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Override
    public SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividade submissao) {

        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SaladeAula sala = atividade.getSalaDeAula();

        boolean membro = sala.getUsuarios().stream()
                .anyMatch(u -> u.getId().equals(alunoId));

        if (!membro) {
            throw new SecurityException("Este usuário não pertence à sala dessa atividade.");
        }

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

    @Override
    public SubmissaoAtividade enviarSubmissao(Long atividadeId, Long alunoId, SubmissaoAtividadeDTO dto) {
        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SubmissaoAtividade submissao = SubmissaoAtividadeMapper.toEntity(dto, atividade, aluno);
        return enviarSubmissao(atividadeId, alunoId, submissao);
    }

    @Override
    public SubmissaoAtividade enviarSubmissaoComArquivo(Long atividadeId, Long alunoId, String descricao, MultipartFile arquivo) {
        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        SubmissaoAtividade submissao = new SubmissaoAtividade();
        submissao.setDescricao(descricao);

        if (arquivo != null && !arquivo.isEmpty()) {
            try {
                String nomeArquivo = UUID.randomUUID() + "-" + arquivo.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(nomeArquivo);
                Files.write(filePath, arquivo.getBytes());
                submissao.setUrlDocumento("/uploads/" + nomeArquivo);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
            }
        }

        return enviarSubmissao(atividadeId, alunoId, submissao);
    }

    @Override
    public List<SubmissaoAtividade> listarSubmissoesPorAtividade(Long atividadeId) {
        atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        return submissaoRepository.findByAtividadeId(atividadeId);
    }

    @Override
    public SubmissaoAtividade buscarSubmissaoDoAluno(Long atividadeId, Long alunoId) {
        SubmissaoAtividade submissao =
                submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId);

        if (submissao == null) {
            throw new EntityNotFoundException("Submissão não encontrada para este aluno.");
        }

        return submissao;
    }

    @Override
    public SubmissaoAtividade corrigirSubmissao(Long submissaoId, Double nota, String feedback) {
        SubmissaoAtividade submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new EntityNotFoundException("Submissão não encontrada."));

        submissao.setNota(nota);
        submissao.setFeedback(feedback);
        submissao.setDataCorrecao(LocalDateTime.now());

        return submissaoRepository.save(submissao);
    }

    @Override
    public SubmissaoAtividade marcarComoRecebida(Long submissaoId) {
        SubmissaoAtividade submissao = submissaoRepository.findById(submissaoId)
                .orElseThrow(() -> new EntityNotFoundException("Submissão não encontrada."));

        submissao.setRecebida(true);
        submissao.setDataRecebimento(LocalDateTime.now());

        return submissaoRepository.save(submissao);
    }
}
