package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.Professor;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de atividades acadêmicas.
 * 
 * Camada: Application / Business Service (Academic Context)
 * Responsabilidades: Orquestração de criação, listagem e gerenciamento de
 * atividades dentro de salas de aula, com validação de papel (Professor).
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional,
 * Notificação por evento (NotificacaoService).
 * 
 * @see AtividadeService
 * @see docs/domain/academic_context.md
 * @see REQ-020 (Criação de Atividades)
 */
@Service
public class AtividadeServiceImpl implements AtividadeService {

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaladeAulaRepository salaDeAulaRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Cria uma nova atividade acadêmica dentro de uma sala de aula.
     * 
     * @param salaId    ID da sala onde a atividade será criada.
     * @param atividade Entidade da atividade a ser criada.
     * @param autorId   ID do autor (deve ser um professor).
     * @return Atividade criada e persistida.
     * @throws RuntimeException  se a sala ou autor não forem encontrados.
     * @throws SecurityException se o autor não for um professor.
     */
    @Override
    @Transactional
    public Atividade criarAtividade(Long salaId, Atividade atividade, Long autorId) {
        // Passo 1: Verifica se a sala existe
        SaladeAula sala = salaDeAulaRepository.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        // Passo 2: Verifica se o autor existe
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        // Passo 3: Valida se o autor é um professor
        if (!(autor instanceof Professor)) {
            throw new SecurityException("Apenas professores podem criar atividades.");
        }

        // Passo 4: Associa a atividade ao autor e à sala
        atividade.setAutor(autor);
        atividade.setSalaDeAula(sala);

        Atividade saved = atividadeRepository.save(atividade);

        // Notificar alunos sobre nova atividade
        sala.getUsuarios().forEach(usuario -> {
            if (!usuario.getId().equals(autorId)) {
                notificacaoService.criarNotificacao(usuario.getId(), "Nova atividade cadastrada: " + saved.getTitulo(),
                        "ATIVIDADE", saved.getId());
            }
        });

        return saved;
    }

    @Override
    @Transactional
    public Atividade criarAtividade(Long salaId, AtividadeDTO atividadeDTO, Long autorId) {
        Atividade atividade = new Atividade();
        atividade.setTitulo(atividadeDTO.getTitulo());
        atividade.setDescricao(atividadeDTO.getDescricao());
        if (atividadeDTO.getDataEntrega() != null) {
            String dataStr = atividadeDTO.getDataEntrega();
            if (dataStr.contains("T")) {
                atividade.setDataEntrega(java.time.LocalDate.parse(dataStr.substring(0, 10)));
            } else {
                atividade.setDataEntrega(java.time.LocalDate.parse(dataStr));
            }
        }
        return criarAtividade(salaId, atividade, autorId);
    }

    @Override
    public Atividade buscarAtividadePorId(Long atividadeId) {
        return atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    }

    @Override
    public List<Atividade> listarAtividadesPorSala(Long salaId) {
        return atividadeRepository.findBySalaDeAulaId(salaId);
    }

    @Override
    @Transactional
    public Atividade atualizarAtividade(Long atividadeId, Atividade atividadeAtualizada, Long autorId) {
        Atividade existente = buscarAtividadePorId(atividadeId);

        if (!existente.getAutor().getId().equals(autorId)) {
            throw new RuntimeException("Você não tem permissão para atualizar esta atividade");
        }

        existente.setTitulo(atividadeAtualizada.getTitulo());
        existente.setDescricao(atividadeAtualizada.getDescricao());
        existente.setDataEntrega(atividadeAtualizada.getDataEntrega());

        return atividadeRepository.save(existente);
    }

    @Override
    @Transactional
    public Atividade atualizarAtividade(Long atividadeId, AtividadeDTO atividadeDTO, Long autorId) {
        Atividade existente = buscarAtividadePorId(atividadeId);

        if (!existente.getAutor().getId().equals(autorId)) {
            throw new RuntimeException("Você não tem permissão para atualizar esta atividade");
        }

        existente.setTitulo(atividadeDTO.getTitulo());
        existente.setDescricao(atividadeDTO.getDescricao());
        if (atividadeDTO.getDataEntrega() != null) {
            String dataStr = atividadeDTO.getDataEntrega();
            if (dataStr.contains("T")) {
                existente.setDataEntrega(java.time.LocalDate.parse(dataStr.substring(0, 10)));
            } else {
                existente.setDataEntrega(java.time.LocalDate.parse(dataStr));
            }
        }

        return atividadeRepository.save(existente);
    }

    @Override
    @Transactional
    public void deletarAtividade(Long atividadeId, Long autorId) {
        Atividade existente = buscarAtividadePorId(atividadeId);

        if (!existente.getAutor().getId().equals(autorId)) {
            throw new RuntimeException("Você não tem permissão para deletar esta atividade");
        }

        atividadeRepository.delete(existente);
    }

    @Override
    public List<Atividade> listarAtividadesPorAutor(Long autorId) {
        return atividadeRepository.findByAutorId(autorId);
    }
}
