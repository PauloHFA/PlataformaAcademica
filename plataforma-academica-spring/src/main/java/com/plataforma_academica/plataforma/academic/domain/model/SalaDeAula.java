package com.plataforma_academica.plataforma.academic.domain.model;

import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Agregado Raiz que representa uma Sala de Aula Virtual.
 * Modelo Rico: Encapsula comportamento e invariantes.
 * Zero dependências de frameworks.
 */
public final class SalaDeAula {
    private final SalaId id;
    private String nome;
    private final String codigoSala;
    private final UsuarioId criadorId;
    private final List<MembroSala> membros;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    private SalaDeAula(SalaId id, String nome, String codigoSala, UsuarioId criadorId, List<MembroSala> membros,
            LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.codigoSala = codigoSala;
        this.criadorId = criadorId;
        this.membros = new ArrayList<>(membros);
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    private SalaDeAula(SalaId id, String nome, String codigoSala, UsuarioId criadorId) {
        this.id = id;
        this.nome = nome;
        this.codigoSala = codigoSala;
        this.criadorId = criadorId;
        this.membros = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = this.dataCriacao;

        // Invariante: Criador é automaticamente membro com papel DOCENTE
        this.membros.add(new MembroSala(criadorId, PapelMembro.DOCENTE));
    }

    /**
     * Factory Method para criação de nova sala.
     * Gera código único de 8 caracteres alfanuméricos.
     */
    public static SalaDeAula criar(String nome, UsuarioId criadorId) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da sala não pode ser vazio");
        }
        String codigo = gerarCodigoUnico();
        return new SalaDeAula(SalaId.novo(), nome.trim(), codigo, criadorId);
    }

    /**
     * Factory Method para reconstituição a partir do banco de dados.
     */
    public static SalaDeAula reconstituir(SalaId id, String nome, String codigoSala,
            UsuarioId criadorId, List<MembroSala> membros,
            LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        return new SalaDeAula(id, nome, codigoSala, criadorId, membros, dataCriacao, dataAtualizacao);
    }

    private static String gerarCodigoUnico() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Remove chars ambíguos
        StringBuilder sb = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // --- Comportamentos de Negócio (Métodos de Comando) ---

    public void alterarNome(String novoNome) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("Nome da sala não pode ser vazio");
        }
        this.nome = novoNome.trim();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void adicionarMembro(UsuarioId usuarioId, PapelMembro papel) {
        if (usuarioId == null)
            throw new IllegalArgumentException("UsuarioId não pode ser nulo");
        if (papel == null)
            throw new IllegalArgumentException("Papel não pode ser nulo");

        // Invariante: Impedir membros duplicados
        if (membros.stream().anyMatch(m -> m.usuarioId().equals(usuarioId))) {
            throw new IllegalStateException("Usuário já é membro desta sala");
        }

        this.membros.add(new MembroSala(usuarioId, papel));
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void removerMembro(UsuarioId usuarioId, UsuarioId solicitanteId) {
        // Invariante: Apenas docentes ou o próprio aluno podem remover
        boolean isDocente = membros.stream()
                .filter(m -> m.usuarioId().equals(solicitanteId))
                .anyMatch(m -> m.papel() == PapelMembro.DOCENTE);

        boolean isSelf = solicitanteId.equals(usuarioId);

        if (!isDocente && !isSelf) {
            throw new IllegalStateException("Sem permissão para remover este membro");
        }

        // Invariante: Não pode remover o criador
        if (usuarioId.equals(criadorId)) {
            throw new IllegalStateException("Não é possível remover o criador da sala");
        }

        boolean removed = this.membros.removeIf(m -> m.usuarioId().equals(usuarioId));
        if (!removed) {
            throw new IllegalStateException("Membro não encontrado na sala");
        }
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void alterarPapelMembro(UsuarioId usuarioId, PapelMembro novoPapel, UsuarioId solicitanteId) {
        // Apenas docentes podem alterar papéis
        boolean isDocente = membros.stream()
                .filter(m -> m.usuarioId().equals(solicitanteId))
                .anyMatch(m -> m.papel() == PapelMembro.DOCENTE);

        if (!isDocente) {
            throw new IllegalStateException("Apenas docentes podem alterar papéis");
        }

        MembroSala membro = membros.stream()
                .filter(m -> m.usuarioId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Membro não encontrado"));

        // Não permite rebaixar o criador
        if (usuarioId.equals(criadorId) && novoPapel != PapelMembro.DOCENTE) {
            throw new IllegalStateException("Criador deve manter papel DOCENTE");
        }

        membro.alterarPapel(novoPapel);
        this.dataAtualizacao = LocalDateTime.now();
    }

    // --- Consultas (Métodos de Leitura) ---

    public boolean isMembro(UsuarioId usuarioId) {
        return membros.stream().anyMatch(m -> m.usuarioId().equals(usuarioId));
    }

    public boolean isDocente(UsuarioId usuarioId) {
        return membros.stream()
                .filter(m -> m.usuarioId().equals(usuarioId))
                .anyMatch(m -> m.papel() == PapelMembro.DOCENTE);
    }

    public List<UsuarioId> listarAlunos() {
        return membros.stream()
                .filter(m -> m.papel() == PapelMembro.ALUNO)
                .map(MembroSala::usuarioId)
                .collect(Collectors.toList());
    }

    public List<UsuarioId> listarDocentes() {
        return membros.stream()
                .filter(m -> m.papel() == PapelMembro.DOCENTE)
                .map(MembroSala::usuarioId)
                .collect(Collectors.toList());
    }

    // --- Getters (Apenas leitura, coleções imutáveis) ---

    public SalaId id() {
        return id;
    }

    public String nome() {
        return nome;
    }

    public String codigoSala() {
        return codigoSala;
    }

    public UsuarioId criadorId() {
        return criadorId;
    }

    public List<MembroSala> membros() {
        return Collections.unmodifiableList(membros);
    }

    public LocalDateTime dataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime dataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SalaDeAula that = (SalaDeAula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SalaDeAula{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigoSala='" + codigoSala + '\'' +
                ", criadorId=" + criadorId +
                ", totalMembros=" + membros.size() +
                '}';
    }

    // --- Entidade Interna (Inner Entity) ---

    public enum PapelMembro {
        DOCENTE, ALUNO
    }

    public static final class MembroSala {
        private final UsuarioId usuarioId;
        private PapelMembro papel;
        private final LocalDateTime dataEntrada;

        private MembroSala(UsuarioId usuarioId, PapelMembro papel) {
            this.usuarioId = usuarioId;
            this.papel = papel;
            this.dataEntrada = LocalDateTime.now();
        }

        // Construtor de reconstituição
        public static MembroSala reconstituir(UsuarioId usuarioId, PapelMembro papel, LocalDateTime dataEntrada) {
            MembroSala membro = new MembroSala(usuarioId, papel);
            // Como dataEntrada é final, precisamos ajustar o construtor ou usar reflexão.
            // Para manter a pureza, vamos ajustar o construtor privado.
            return new MembroSala(usuarioId, papel, dataEntrada);
        }

        private MembroSala(UsuarioId usuarioId, PapelMembro papel, LocalDateTime dataEntrada) {
            this.usuarioId = usuarioId;
            this.papel = papel;
            this.dataEntrada = dataEntrada;
        }

        void alterarPapel(PapelMembro novoPapel) {
            this.papel = novoPapel;
        }

        public UsuarioId usuarioId() {
            return usuarioId;
        }

        public PapelMembro papel() {
            return papel;
        }

        public LocalDateTime dataEntrada() {
            return dataEntrada;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MembroSala that = (MembroSala) o;
            return Objects.equals(usuarioId, that.usuarioId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(usuarioId);
        }
    }
}
