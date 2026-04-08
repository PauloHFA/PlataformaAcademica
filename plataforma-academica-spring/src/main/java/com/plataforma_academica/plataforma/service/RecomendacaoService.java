package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.RecomendacaoUsuarioDTO;
import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.InteracaoUsuarioRepository;
import com.plataforma_academica.plataforma.repository.RecomendacaoUsuarioRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomendacaoService {

    private final UsuarioRepository usuarioRepository;
    private final InteracaoUsuarioRepository interacaoRepository;
    private final RecomendacaoUsuarioRepository recomendacaoRepository;

    /**
     * Gera recomendações de usuários para um usuário específico
     */
    @Transactional
    public List<RecomendacaoUsuarioDTO> gerarRecomendacoes(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca usuários similares baseado em interações
        List<Usuario> usuariosSimilares = encontrarUsuariosSimilares(usuario);

        // Calcula scores de similaridade e cria recomendações
        List<RecomendacaoUsuario> recomendacoes = new ArrayList<>();

        for (Usuario usuarioSimilar : usuariosSimilares) {
            if (usuarioSimilar.getId().equals(usuarioId)) continue;

            double score = calcularScoreSimilaridade(usuario, usuarioSimilar);
            String motivo = determinarMotivoRecomendacao(usuario, usuarioSimilar);

            RecomendacaoUsuario recomendacao = new RecomendacaoUsuario();
            recomendacao.setUsuario(usuario);
            recomendacao.setUsuarioRecomendado(usuarioSimilar);
            recomendacao.setScoreSimilaridade(score);
            recomendacao.setMotivoRecomendacao(motivo);
            recomendacao.setTipoRecomendacao(determinarTipoRecomendacao(usuario, usuarioSimilar));

            recomendacoes.add(recomendacao);
        }

        // Salva as recomendações
        recomendacaoRepository.saveAll(recomendacoes);

        return recomendacoes.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Busca recomendações existentes para um usuário
     */
    public List<RecomendacaoUsuarioDTO> buscarRecomendacoes(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<RecomendacaoUsuario> recomendacoes = recomendacaoRepository
            .findByUsuarioAndAtivoOrderByScoreSimilaridadeDesc(usuario, true);

        return recomendacoes.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Registra uma interação do usuário para melhorar recomendações futuras
     */
    @Transactional
    public void registrarInteracao(Long usuarioId, InteracaoUsuario.TipoInteracao tipo,
                                 String entidadeTipo, Long entidadeId, String tags) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        InteracaoUsuario interacao = new InteracaoUsuario();
        interacao.setUsuario(usuario);
        interacao.setTipoInteracao(tipo);
        interacao.setEntidadeTipo(entidadeTipo);
        interacao.setEntidadeId(entidadeId);
        interacao.setPesoInteracao(calcularPesoInteracao(tipo));
        interacao.setTags(tags);

        interacaoRepository.save(interacao);
    }

    /**
     * Encontra usuários similares baseado em padrões de interação
     */
    private List<Usuario> encontrarUsuariosSimilares(Usuario usuario) {
        // Busca usuários que interagiram com as mesmas entidades
        List<InteracaoUsuario> interacoesUsuario = interacaoRepository
            .findByUsuarioAndDataInteracaoAfter(usuario, LocalDateTime.now().minusDays(30));

        Set<Long> entidadesInteragidas = interacoesUsuario.stream()
            .map(InteracaoUsuario::getEntidadeId)
            .collect(Collectors.toSet());

        // Encontra outros usuários que interagiram com essas entidades
        List<Usuario> usuariosSimilares = new ArrayList<>();
        for (Long entidadeId : entidadesInteragidas) {
            List<Usuario> usuariosDaEntidade = interacaoRepository
                .findUsuariosByEntidade("POSTAGEM", entidadeId);
            usuariosSimilares.addAll(usuariosDaEntidade);
        }

        // Remove duplicatas e o próprio usuário
        return usuariosSimilares.stream()
            .filter(u -> !u.getId().equals(usuario.getId()))
            .distinct()
            .limit(20)
            .collect(Collectors.toList());
    }

    /**
     * Calcula score de similaridade entre dois usuários usando algoritmo de similaridade cosseno
     */
    private double calcularScoreSimilaridade(Usuario usuario1, Usuario usuario2) {
        List<InteracaoUsuario> interacoes1 = interacaoRepository
            .findByUsuarioAndDataInteracaoAfter(usuario1, LocalDateTime.now().minusDays(30));
        List<InteracaoUsuario> interacoes2 = interacaoRepository
            .findByUsuarioAndDataInteracaoAfter(usuario2, LocalDateTime.now().minusDays(30));

        // Cria vetores de frequência de interações por tipo
        Map<InteracaoUsuario.TipoInteracao, Double> vetor1 = criarVetorInteracoes(interacoes1);
        Map<InteracaoUsuario.TipoInteracao, Double> vetor2 = criarVetorInteracoes(interacoes2);

        // Calcula similaridade cosseno
        return calcularSimilaridadeCosseno(vetor1, vetor2);
    }

    private Map<InteracaoUsuario.TipoInteracao, Double> criarVetorInteracoes(List<InteracaoUsuario> interacoes) {
        Map<InteracaoUsuario.TipoInteracao, Double> vetor = new HashMap<>();
        for (InteracaoUsuario interacao : interacoes) {
            vetor.merge(interacao.getTipoInteracao(),
                       interacao.getPesoInteracao(),
                       Double::sum);
        }
        return vetor;
    }

    private double calcularSimilaridadeCosseno(Map<InteracaoUsuario.TipoInteracao, Double> vetor1,
                                             Map<InteracaoUsuario.TipoInteracao, Double> vetor2) {
        Set<InteracaoUsuario.TipoInteracao> todasInteracoes = new HashSet<>();
        todasInteracoes.addAll(vetor1.keySet());
        todasInteracoes.addAll(vetor2.keySet());

        double produtoEscalar = 0.0;
        double norma1 = 0.0;
        double norma2 = 0.0;

        for (InteracaoUsuario.TipoInteracao tipo : todasInteracoes) {
            double valor1 = vetor1.getOrDefault(tipo, 0.0);
            double valor2 = vetor2.getOrDefault(tipo, 0.0);

            produtoEscalar += valor1 * valor2;
            norma1 += valor1 * valor1;
            norma2 += valor2 * valor2;
        }

        if (norma1 == 0.0 || norma2 == 0.0) return 0.0;

        return produtoEscalar / (Math.sqrt(norma1) * Math.sqrt(norma2));
    }

    private String determinarMotivoRecomendacao(Usuario usuario1, Usuario usuario2) {
        // Lógica simples para determinar motivo (pode ser expandida)
        List<InteracaoUsuario> interacoes1 = interacaoRepository
            .findByUsuarioAndDataInteracaoAfter(usuario1, LocalDateTime.now().minusDays(7));
        List<InteracaoUsuario> interacoes2 = interacaoRepository
            .findByUsuarioAndDataInteracaoAfter(usuario2, LocalDateTime.now().minusDays(7));

        Set<Long> entidades1 = interacoes1.stream()
            .map(InteracaoUsuario::getEntidadeId)
            .collect(Collectors.toSet());
        Set<Long> entidades2 = interacoes2.stream()
            .map(InteracaoUsuario::getEntidadeId)
            .collect(Collectors.toSet());

        entidades1.retainAll(entidades2); // Interseção

        if (!entidades1.isEmpty()) {
            return "Interesses similares em " + entidades1.size() + " tópicos";
        }

        return "Padrões de interação similares";
    }

    private RecomendacaoUsuario.TipoRecomendacao determinarTipoRecomendacao(Usuario usuario1, Usuario usuario2) {
        // Lógica para determinar tipo de recomendação
        return RecomendacaoUsuario.TipoRecomendacao.ESTUDO_GRUPO;
    }

    private double calcularPesoInteracao(InteracaoUsuario.TipoInteracao tipo) {
        switch (tipo) {
            case CURTIDA: return 1.0;
            case COMENTARIO: return 2.0;
            case COMPARTILHAMENTO: return 3.0;
            case PARTICIPACAO_ATIVIDADE: return 4.0;
            case ENTRADA_COMUNIDADE: return 2.5;
            case ACEITACAO_AMIZADE: return 5.0;
            default: return 1.0;
        }
    }

    private RecomendacaoUsuarioDTO convertToDTO(RecomendacaoUsuario recomendacao) {
        RecomendacaoUsuarioDTO dto = new RecomendacaoUsuarioDTO();
        dto.setId(recomendacao.getId());
        dto.setScoreSimilaridade(recomendacao.getScoreSimilaridade());
        dto.setMotivoRecomendacao(recomendacao.getMotivoRecomendacao());
        dto.setTipoRecomendacao(recomendacao.getTipoRecomendacao().toString());
        dto.setDataCriacao(recomendacao.getDataCriacao());
        dto.setAtivo(recomendacao.getAtivo());

        // Usuario atual
        RecomendacaoUsuarioDTO.UsuarioDTO usuarioDTO = new RecomendacaoUsuarioDTO.UsuarioDTO();
        usuarioDTO.setId(recomendacao.getUsuario().getId());
        usuarioDTO.setNome(recomendacao.getUsuario().getNome());
        usuarioDTO.setEmail(recomendacao.getUsuario().getEmail());
        dto.setUsuario(usuarioDTO);

        // Usuario recomendado
        RecomendacaoUsuarioDTO.UsuarioDTO recomendadoDTO = new RecomendacaoUsuarioDTO.UsuarioDTO();
        recomendadoDTO.setId(recomendacao.getUsuarioRecomendado().getId());
        recomendadoDTO.setNome(recomendacao.getUsuarioRecomendado().getNome());
        recomendadoDTO.setEmail(recomendacao.getUsuarioRecomendado().getEmail());
        recomendadoDTO.setFotoPerfil(recomendacao.getUsuarioRecomendado().getFotoPerfil());
        dto.setUsuarioRecomendado(recomendadoDTO);

        return dto;
    }
}