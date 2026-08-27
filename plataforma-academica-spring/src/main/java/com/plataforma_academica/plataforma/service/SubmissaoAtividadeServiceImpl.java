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
import com.plataforma_academica.plataforma.service.NotificacaoService;
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

/**
 * Implementação do serviço de submissões de atividades.
 * 
 * Camada: Application Service
 * Responsabilidades: Receber, avaliar e gerenciar submissões de alunos em
 * atividades.
 */
/**
 * Implementação do serviço de Submissões de Atividades.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional.
 * 
 * @see SubmissaoAtividadeService
 * @see docs/domain/academic_context.md
 * @see REQ-026 (Submissão e Avaliação de Atividades)
 */
@Service
public class SubmissaoAtividadeServiceImpl implements SubmissaoAtividadeService {

    private final SubmissaoAtividadeRespository submissaoRepository;
    private final AtividadeRepository atividadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public SubmissaoAtividadeServiceImpl(
            SubmissaoAtividadeRespository submissaoRepository,
            AtividadeRepository atividadeRepository,
            UsuarioRepository usuarioRepository,
            NotificacaoService notificacaoService) {
        this.submissaoRepository = submissaoRepository;
        this.atividadeRepository = atividadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
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

        SubmissaoAtividade savedSubmissao = submissaoRepository.save(submissao);

        // Notificar professor sobre nova submissão
        String mensagem = "Nova submissão recebida de " + aluno.getNome() + " na atividade '" + atividade.getTitulo()
                + "'";
        notificacaoService.criarNotificacao(atividade.getAutor().getId(), mensagem, "SUBMISSAO",
                savedSubmissao.getId());

        return savedSubmissao;
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
    public SubmissaoAtividade enviarSubmissaoComArquivo(Long atividadeId, Long alunoId, String descricao,
            MultipartFile arquivo) {
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
    public List<SubmissaoAtividade> listarSubmissoesPorAluno(Long alunoId) {
        usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        return submissaoRepository.findByAlunoId(alunoId);
    }

    @Override
    public List<SubmissaoAtividade> listarSubmissoesPorAlunoESala(Long alunoId, Long salaId) {
        usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        // Não há necessidade de buscar sala explicitamente; o filtro por sala funciona
        // na query do repository
        return submissaoRepository.findByAlunoIdAndAtividade_SalaDeAula_Id(alunoId, salaId);
    }

    @Override
    public SubmissaoAtividade buscarSubmissaoDoAluno(Long atividadeId, Long alunoId) {
        SubmissaoAtividade submissao = submissaoRepository.findByAtividadeIdAndAlunoId(atividadeId, alunoId);

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

        SubmissaoAtividade saved = submissaoRepository.save(submissao);

        // Notificar aluno sobre correção
        String mensagem = "Sua atividade '" + submissao.getAtividade().getTitulo() + "' foi corrigida.";
        if (nota != null) {
            mensagem += " Nota: " + nota;
        }
        if (feedback != null && !feedback.isEmpty()) {
            mensagem += " Feedback: " + feedback;
        }
        notificacaoService.criarNotificacao(submissao.getAluno().getId(), mensagem, "CORRECAO", saved.getId());

        return saved;
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
