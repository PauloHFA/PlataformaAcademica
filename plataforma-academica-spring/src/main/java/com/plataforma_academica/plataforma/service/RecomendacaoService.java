package com.plataforma_academica.plataforma.service;

import java.util.UUID;

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

/**
 * Serviço de aplicação responsável por gerenciar o ciclo de vida das
 * recomendações de usuários.
 * 
 * Camada: Application Service
 * Responsabilidades:
 * - Gerar recomendações personalizadas usando similaridade de interações
 * (algoritmo cosseno).
 * - Persistir recomendações geradas.
 * - Registrar interações de usuários para alimentar o modelo de recomendação.
 * 
 * Padrões aplicados: Service Layer, Transacional (@Transactional).
 */
@Service
@RequiredArgsConstructor
public class RecomendacaoService {

    private final UsuarioRepository usuarioRepository;
    private final InteracaoUsuarioRepository interacaoRepository;
    private final RecomendacaoUsuarioRepository recomendacaoRepository;

    /**
     * Gera recomendações de usuários para um usuário específico com base em
     * similaridade de interações.
     * 
     * Fluxo:
     * 1. Recupera o usuário alvo.
     * 2. Encontra usuários similares baseado nas interações dos últimos 30 dias.
     * 3. Para cada usuário similar, calcula score de similaridade cosseno e
     * determina motivo/tipo.
     * 4. Cria e persiste as entidades de recomendação.
     * 5. Converte as entidades para DTOs e retorna a lista ordenada por score.
     * 
     * @param usuarioId ID do usuário para o qual as recomendações serão geradas.
     * @return Lista de DTOs contendo as recomendações com scores e motivos.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public List<RecomendacaoUsuarioDTO> gerarRecomendacoes(UUID usuarioId) {
        // Passo 1: Validar e recuperar a entidade raiz do usuário alvo
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Passo 2: Busca usuários similares baseado em interações recentes
        List<Usuario> usuariosSimilares = encontrarUsuariosSimilares(usuario);

        // Passo 3: Calcula scores de similaridade e cria recomendações
        List<RecomendacaoUsuario> recomendacoes = new ArrayList<>();

        for (Usuario usuarioSimilar : usuariosSimilares) {
            // Ignora o próprio usuário na lista de similares
            if (usuarioSimilar.getId().equals(usuarioId))
                continue;

            // Calcula o score de similaridade cosseno entre os vetores de interação
            double score = calcularScoreSimilaridade(usuario, usuarioSimilar);
            // Determina o motivo legível da recomendação
            String motivo = determinarMotivoRecomendacao(usuario, usuarioSimilar);

            // Cria a entidade de recomendação
            RecomendacaoUsuario recomendacao = new RecomendacaoUsuario();
            recomendacao.setUsuario(usuario);
            recomendacao.setUsuarioRecomendado(usuarioSimilar);
            recomendacao.setScoreSimilaridade(score);
            recomendacao.setMotivoRecomendacao(motivo);
            recomendacao.setTipoRecomendacao(determinarTipoRecomendacao(usuario, usuarioSimilar));

            recomendacoes.add(recomendacao);
        }

        // Passo 4: Persiste as recomendações geradas
        recomendacaoRepository.saveAll(recomendacoes);

        // Passo 5: Converte para DTO e retorna
        return recomendacoes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca recomendações existentes e ativas para um usuário, ordenadas por score
     * decrescente.
     * 
     * @param usuarioId ID do usuário cuyas recomendações serão buscadas.
     * @return Lista de DTOs de recomendações ativas.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    public List<RecomendacaoUsuarioDTO> buscarRecomendacoes(UUID usuarioId) {
        // Recupera o usuário para validação de existência
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Busca recomendações ativas ordenadas por score (maior primeiro)
        List<RecomendacaoUsuario> recomendacoes = recomendacaoRepository
                .findByUsuarioAndAtivoOrderByScoreSimilaridadeDesc(usuario, true);

        // Converte entidades para DTOs
        return recomendacoes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Registra uma interação do usuário para melhorar o modelo de recomendação
     * futuro.
     * 
     * A interação é persistida com um peso baseado no tipo (ex: curtida=1,
     * comentário=2, etc.).
     * 
     * @param usuarioId    ID do usuário que realizou a interação.
     * @param tipo         Tipo da interação (CURTIDA, COMENTARIO, etc.).
     * @param entidadeTipo Tipo da entidade com a qual interagiu (ex: POSTAGEM,
     *                     ATIVIDADE).
     * @param entidadeId   ID da entidade com a qual interagiu.
     * @param tags         Tags opcionais associadas à interação para filtragem
     *                     futura.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public void registrarInteracao(UUID usuarioId, InteracaoUsuario.TipoInteracao tipo,
            String entidadeTipo, UUID entidadeId, String tags) {
        // Valida e recupera o usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Cria a entidade de interação
        InteracaoUsuario interacao = new InteracaoUsuario();
        interacao.setUsuario(usuario);
        interacao.setTipoInteracao(tipo);
        interacao.setEntidadeTipo(entidadeTipo);
        interacao.setEntidadeId(entidadeId);
        // Define o peso da interação baseado no tipo
        interacao.setPesoInteracao(calcularPesoInteracao(tipo));
        interacao.setTags(tags);

        // Persiste a interação
        interacaoRepository.save(interacao);
    }

    /**
     * Encontra usuários similares baseado em padrões de interação nas últimas 30
     * dias.
     * 
     * Algoritmo:
     * 1. Coleta todas as interações do usuário nos últimos 30 dias.
     * 2. Extrai o conjunto único de IDs de entidades com as quais o usuário
     * interagiu.
     * 3. Para cada entidade, busca outros usuários que também interagiram com ela.
     * 4. Junta os resultados, remove duplicatas e o próprio usuário, limitando a 20
     * similares.
     * 
     * @param usuario Usuário de referência para encontrar similares.
     * @return Lista de usuários similares (máximo 20).
     */
    private List<Usuario> encontrarUsuariosSimilares(Usuario usuario) {
        // Passo 1: Busca interações do usuário nos últimos 30 dias
        List<InteracaoUsuario> interacoesUsuario = interacaoRepository
                .findByUsuarioAndDataInteracaoAfter(usuario, LocalDateTime.now().minusDays(30));

        // Passo 2: Extrai o conjunto de entidades com as quais o usuário interagiu
        Set<UUID> entidadesInteragidas = interacoesUsuario.stream()
                .map(InteracaoUsuario::getEntidadeId)
                .collect(Collectors.toSet());

        // Passo 3: Encontra outros usuários que interagiram com essas mesmas entidades
        List<Usuario> usuariosSimilares = new ArrayList<>();
        for (UUID entidadeId : entidadesInteragidas) {
            // Busca usuários que interagiram com a entidade atual
            List<Usuario> usuariosDaEntidade = interacaoRepository
                    .findUsuariosByEntidade("POSTAGEM", entidadeId);
            usuariosSimilares.addAll(usuariosDaEntidade);
        }

        // Passo 4: Remove duplicatas, exclui o próprio usuário e limita o resultado
        return usuariosSimilares.stream()
                .filter(u -> !u.getId().equals(usuario.getId())) // Remove o próprio usuário
                .distinct() // Remove duplicatas
                .limit(20) // Limita a 20 similares para performance
                .collect(Collectors.toList());
    }

    /**
     * Calcula o score de similaridade entre dois usuários usando o algoritmo de
     * Cosseno.
     * 
     * Cada usuário é representado por um vetor onde cada dimensão é um tipo de
     * interação
     * e o valor é o peso total das interações desse tipo nos últimos 30 dias.
     * 
     * Similaridade de Cosseno = (A · B) / (||A|| * ||B||)
     * 
     * @param usuario1 Primeiro usuário.
     * @param usuario2 Segundo usuário.
     * @return Valor entre 0.0 e 1.0 representando a similaridade (0 = nenhum, 1 =
     *         idêntico).
     */
    private double calcularScoreSimilaridade(Usuario usuario1, Usuario usuario2) {
        // Passo 1: Recupera interações recentes de ambos os usuários
        List<InteracaoUsuario> interacoes1 = interacaoRepository
                .findByUsuarioAndDataInteracaoAfter(usuario1, LocalDateTime.now().minusDays(30));
        List<InteracaoUsuario> interacoes2 = interacaoRepository
                .findByUsuarioAndDataInteracaoAfter(usuario2, LocalDateTime.now().minusDays(30));

        // Passo 2: Converte as listas de interações em vetores de pesos por tipo de
        // interação
        Map<InteracaoUsuario.TipoInteracao, Double> vetor1 = criarVetorInteracoes(interacoes1);
        Map<InteracaoUsuario.TipoInteracao, Double> vetor2 = criarVetorInteracoes(interacoes2);

        // Passo 3: Calcula a similaridade de Cosseno entre os dois vetores
        return calcularSimilaridadeCosseno(vetor1, vetor2);
    }

    /**
     * Converte uma lista de interações em um vetor de pesos por tipo de interação.
     * 
     * Cada entrada do mapa representa: TipoInteracao -> soma dos pesos das
     * interações desse tipo.
     * 
     * @param interacoes Lista de interações a serem agregadas.
     * @return Mapa contendo o peso total por tipo de interação.
     */
    private Map<InteracaoUsuario.TipoInteracao, Double> criarVetorInteracoes(List<InteracaoUsuario> interacoes) {
        Map<InteracaoUsuario.TipoInteracao, Double> vetor = new HashMap<>();
        for (InteracaoUsuario interacao : interacoes) {
            // Soma o peso da interação ao tipo correspondente no vetor
            vetor.merge(interacao.getTipoInteracao(),
                    interacao.getPesoInteracao(),
                    Double::sum);
        }
        return vetor;
    }

    /**
     * Calcula a similaridade de Cosseno entre dois vetores esparsos representados
     * por mapas.
     * 
     * Fórmula: (A·B) / (||A|| * ||B||)
     * onde A·B é o produto escalar e ||A|| é a norma euclidiana do vetor A.
     * 
     * @param vetor1 Primeiro vetor (tipo -> peso).
     * @param vetor2 Segundo vetor (tipo -> peso).
     * @return Similaridade de Cosseno entre 0.0 e 1.0.
     */
    private double calcularSimilaridadeCosseno(Map<InteracaoUsuario.TipoInteracao, Double> vetor1,
            Map<InteracaoUsuario.TipoInteracao, Double> vetor2) {
        // Passo 1: Determina o conjunto union de todas as chaves (tipos de interação)
        // presentes em qualquer vetor
        Set<InteracaoUsuario.TipoInteracao> todasInteracoes = new HashSet<>();
        todasInteracoes.addAll(vetor1.keySet());
        todasInteracoes.addAll(vetor2.keySet());

        double produtoEscalar = 0.0;
        double norma1 = 0.0;
        double norma2 = 0.0;

        // Passo 2: Itera sobre cada tipo de interação para calcular produto escalar e
        // normas
        for (InteracaoUsuario.TipoInteracao tipo : todasInteracoes) {
            double valor1 = vetor1.getOrDefault(tipo, 0.0); // Peso do tipo no vetor 1 (0 se ausente)
            double valor2 = vetor2.getOrDefault(tipo, 0.0); // Peso do tipo no vetor 2 (0 se ausente)

            // Produto escalar: soma de (Ai * Bi)
            produtoEscalar += valor1 * valor2;
            // Norma ao quadrado: soma de Ai^2 e Bi^2
            norma1 += valor1 * valor1;
            norma2 += valor2 * valor2;
        }

        // Passo 3: Evita divisão por zero (vetor nulo)
        if (norma1 == 0.0 || norma2 == 0.0)
            return 0.0;

        // Passo 4: Aplica a fórmula da similaridade de Cosseno
        return produtoEscalar / (Math.sqrt(norma1) * Math.sqrt(norma2));
    }

    /**
     * Determina um motivo legível para a recomendação baseado nas interações
     * recentes comuns.
     * 
     * Analisa as interações dos últimos 7 dias de ambos os usuários e identifica
     * entidades
     * com as quais ambos interagiram (interseção). Se houver interseção, retorna
     * uma mensagem
     * com a quantidade de tópicos em comum; caso contrário, retorna um motivo
     * genérico.
     * 
     * @param usuario1 Primeiro usuário.
     * @param usuario2 Segundo usuário.
     * @string Motivo da recomendação.
     */
    private String determinarMotivoRecomendacao(Usuario usuario1, Usuario usuario2) {
        // Passo 1: Busca interações recentes (últimos 7 dias) de ambos os usuários
        List<InteracaoUsuario> interacoes1 = interacaoRepository
                .findByUsuarioAndDataInteracaoAfter(usuario1, LocalDateTime.now().minusDays(7));
        List<InteracaoUsuario> interacoes2 = interacaoRepository
                .findByUsuarioAndDataInteracaoAfter(usuario2, LocalDateTime.now().minusDays(7));

        // Passo 2: Extrai os conjuntos de entidades com as quais cada usuário interagiu
        Set<UUID> entidades1 = interacoes1.stream()
                .map(InteracaoUsuario::getEntidadeId)
                .collect(Collectors.toSet());
        Set<UUID> entidades2 = interacoes2.stream()
                .map(InteracaoUsuario::getEntidadeId)
                .collect(Collectors.toSet());

        // Passo 3: Calcula a interseção (entidades comuns)
        entidades1.retainAll(entidades2); // Interseção

        // Passo 4: Formata o motivo baseado na interseção
        if (!entidades1.isEmpty()) {
            return "Interesses similares em " + entidades1.size() + " tópicos";
        }

        return "Padrões de interação similares";
    }

    /**
     * Determina o tipo de recomendação baseado no contexto dos usuários.
     * 
     * Implementação atual retorna sempre ESTUDO_GRUPO como placeholder.
     * Futuras implementações podem considerar perfis, cursos, interesses, etc.
     * 
     * @param usuario1 Primeiro usuário.
     * @param usuario2 Segundo usuário.
     * @return Tipo de recomendação.
     */
    private RecomendacaoUsuario.TipoRecomendacao determinarTipoRecomendacao(Usuario usuario1, Usuario usuario2) {
        // TODO: Implementar lógica baseada em perfis, cursos, interesses comuns, etc.
        return RecomendacaoUsuario.TipoRecomendacao.ESTUDO_GRUPO;
    }

    /**
     * Retorna o peso numérico associado a cada tipo de interação.
     * 
     * Os pesos refletem o nível de engajamento ou esforço da interação:
     * - CURTIDA: 1.0 (baixo engajamento)
     * - COMENTARIO: 2.0 (engajamento médio)
     * - COMPARTILHAMENTO: 3.0 (alto engajamento)
     * - PARTICIPACAO_ATIVIDADE: 4.0 (participação ativa em atividade)
     * - ENTRADA_COMUNIDADE: 2.5 (entrada em comunidade)
     * - ACEITACAO_AMIZADE: 5.0 (conexão social forte)
     * 
     * @param tipo Tipo de interação.
     * @return Peso da interação.
     */
    private double calcularPesoInteracao(InteracaoUsuario.TipoInteracao tipo) {
        switch (tipo) {
            case CURTIDA:
                return 1.0;
            case COMENTARIO:
                return 2.0;
            case COMPARTILHAMENTO:
                return 3.0;
            case PARTICIPACAO_ATIVIDADE:
                return 4.0;
            case ENTRADA_COMUNIDADE:
                return 2.5;
            case ACEITACAO_AMIZADE:
                return 5.0;
            default:
                return 1.0; // Peso padrão para tipos futuros ou desconhecidos
        }
    }

    /**
     * Converte uma entidade de recomendação para seu DTO de representação.
     * 
     * Mapeia os campos da entidade RecomendacaoUsuario para RecomendacaoUsuarioDTO,
     * incluindo os dados resumidos dos usuários envolvidos.
     * 
     * @param recomendacao Entidade de recomendação a ser convertida.
     * @return DTO contendo os dados da recomendação.
     */
    private RecomendacaoUsuarioDTO convertToDTO(RecomendacaoUsuario recomendacao) {
        RecomendacaoUsuarioDTO dto = new RecomendacaoUsuarioDTO();
        dto.setId(recomendacao.getId());
        dto.setScoreSimilaridade(recomendacao.getScoreSimilaridade());
        dto.setMotivoRecomendacao(recomendacao.getMotivoRecomendacao());
        dto.setTipoRecomendacao(recomendacao.getTipoRecomendacao().toString());
        dto.setDataCriacao(recomendacao.getDataCriacao());
        dto.setAtivo(recomendacao.getAtivo());

        // Mapeia os dados do usuário que recebeu a recomendação
        RecomendacaoUsuarioDTO.UsuarioDTO usuarioDTO = new RecomendacaoUsuarioDTO.UsuarioDTO();
        usuarioDTO.setId(recomendacao.getUsuario().getId());
        usuarioDTO.setNome(recomendacao.getUsuario().getNome());
        usuarioDTO.setEmail(recomendacao.getUsuario().getEmail());
        dto.setUsuario(usuarioDTO);

        // Mapeia os dados do usuário recomendado
        RecomendacaoUsuarioDTO.UsuarioDTO recomendadoDTO = new RecomendacaoUsuarioDTO.UsuarioDTO();
        recomendadoDTO.setId(recomendacao.getUsuarioRecomendado().getId());
        recomendadoDTO.setNome(recomendacao.getUsuarioRecomendado().getNome());
        recomendadoDTO.setEmail(recomendacao.getUsuarioRecomendado().getEmail());
        recomendadoDTO.setFotoPerfil(recomendacao.getUsuarioRecomendado().getFotoPerfil());
        dto.setUsuarioRecomendado(recomendadoDTO);

        return dto;
    }
}