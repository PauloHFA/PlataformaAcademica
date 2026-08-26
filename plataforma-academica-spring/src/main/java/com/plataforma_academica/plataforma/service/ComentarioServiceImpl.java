package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ComentarioDTO;
import com.plataforma_academica.plataforma.mapper.ComentarioMapper;
import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço de comentários.
 * 
 * Camada: Application / Business Service (Social Context)
 * Responsabilidades: Orquestração de casos de uso para comentários (salvar,
 * buscar, listar, atualizar) vinculados a postagens, atividades ou salas de
 * aula.
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional.
 * 
 * @see ComentarioService
 * @see docs/domain/social_context.md
 * @see REQ-030 (Sistema de Comentários)
 */
@Service
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private AtividadeRepository atividadeRepository;

    @Autowired
    private SaladeAulaRepository saladeAulaRepository;

    /**
     * Salva um novo comentário, definindo a data de criação se não informada.
     * 
     * @param comentario Entidade do comentário a ser salvo.
     * @return Comentário persistido.
     */
    @Override
    public Comentario salvar(Comentario comentario) {
        // Passo 1: Define a data de criação se não informada
        if (comentario.getDataCriacao() == null) {
            comentario.setDataCriacao(java.time.LocalDateTime.now());
        }
        // Passo 2: Log para rastreamento de conteúdo e contexto
        System.out.println("Salvando comentário: " + comentario.getConteudo());
        System.out
                .println("Sala: " + (comentario.getSaladeAula() != null ? comentario.getSaladeAula().getId() : "null"));
        System.out.println("Tipo destino: " + comentario.getTipoDestino());
        // Passo 3: Persiste o comentário
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }

    @Override
    public Comentario atualizar(Long id, Comentario comentarioAtualizado) {
        Comentario existente = buscarPorId(id);

        if (existente == null) {
            return null;
        }

        existente.setConteudo(comentarioAtualizado.getConteudo());
        existente.setAutor(comentarioAtualizado.getAutor());
        existente.setPostagem(comentarioAtualizado.getPostagem());

        return comentarioRepository.save(existente);
    }

    @Override
    public void deletar(Long id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public List<Comentario> listarComentariosPorSala(Long salaId) {
        return comentarioRepository.findBySaladeAulaId(salaId);
    }

    @Override
    public List<Comentario> listarComentariosPorAtividade(Long atividadeId) {
        return comentarioRepository.findByAtividadeId(atividadeId);
    }

    @Override
    public List<Comentario> listarComentariosPorPostagem(Long postagemId) {
        return comentarioRepository.findByPostagemId(postagemId);
    }

    @Override
    public Comentario salvarComentario(ComentarioDTO dto) {
        // Carregar autor
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        // Parse tipo destino
        TipoDestinoComentario tipoDestino = TipoDestinoComentario.valueOf(dto.getTipoDestino().toUpperCase());

        // Carregar entidades baseadas no tipo destino
        Postagem postagem = null;
        Atividade atividade = null;
        SaladeAula sala = null;

        switch (tipoDestino) {
            case POSTAGEM:
                if (dto.getPostagemId() == null) {
                    throw new IllegalArgumentException("Postagem é obrigatória para comentários em postagens");
                }
                postagem = postagemRepository.findById(dto.getPostagemId())
                        .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
                break;
            case ATIVIDADE:
                if (dto.getAtividadeId() == null) {
                    throw new IllegalArgumentException("Atividade é obrigatória para comentários em atividades");
                }
                atividade = atividadeRepository.findById(dto.getAtividadeId())
                        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
                break;
            case SALADEAULA:
                if (dto.getSalaId() == null) {
                    throw new IllegalArgumentException("Sala é obrigatória para comentários em salas");
                }
                sala = saladeAulaRepository.findById(dto.getSalaId())
                        .orElseThrow(() -> new RuntimeException("Sala não encontrada"));
                break;
            case ATIVIDADES_GERAIS:
                // Não precisa de entidade específica
                break;
        }

        // Criar entidade Comentario
        Comentario comentario = ComentarioMapper.toEntity(dto, autor, postagem, atividade, sala, tipoDestino);

        // Salvar
        return this.salvar(comentario);
    }

    public Comentario salvarComentarioSala(Comentario comentario) {
        if (comentario.getSaladeAula() == null || comentario.getSaladeAula().getId() == null) {
            throw new IllegalArgumentException("Sala de aula é obrigatória");
        }
        if (comentario.getAutor() == null || comentario.getAutor().getId() == null) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        comentario.setTipoDestino(com.plataforma_academica.plataforma.model.TipoDestinoComentario.SALADEAULA);
        return this.salvar(comentario);
    }
}
