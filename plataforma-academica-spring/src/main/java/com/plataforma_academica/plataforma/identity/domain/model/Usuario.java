package com.plataforma_academica.plataforma.identity.domain.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Agregado raiz que representa o membro cadastrado no ecossistema.
 * 
 * Detentor de credenciais, perfil e saldo de gamificação.
 * Raiz do Agregado (Aggregate Root).
 * 
 * Responsabilidades:
 * - Gerenciar credenciais de acesso (senha).
 * - Gerenciar perfil público do usuário.
 * - Gerenciar papéis de acesso (RBAC).
 * - Gerenciar saldo de gamificação (pontos e moedas).
 * 
 * Padrão aplicado: Aggregate Root (Modelo Rico).
 */
public final class Usuario {
    private final UsuarioId id;
    private final Email email;
    private SenhaHash senhaHash;
    private final String nome;
    private PerfilUsuario perfil;
    private final Set<Papel> papeis;
    private SaldoGamificacao saldo;
    private final LocalDateTime dataCadastro;
    private LocalDateTime ultimoLogin;

    /**
     * Construtor privado para criação de novo usuário.
     */
    private Usuario(UsuarioId id, Email email, SenhaHash senhaHash, String nome, Papel papelInicial) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nome = nome;
        this.perfil = PerfilUsuario.vazio();
        this.papeis = new HashSet<>();
        this.papeis.add(papelInicial);
        this.saldo = SaldoGamificacao.zerado();
        this.dataCadastro = LocalDateTime.now();
        this.ultimoLogin = null;
    }

    /**
     * Construtor privado para reconstituição de estado (usado pelo Mapper).
     */
    private Usuario(UsuarioId id, Email email, SenhaHash senhaHash, String nome, PerfilUsuario perfil,
            Set<Papel> papeis, SaldoGamificacao saldo, LocalDateTime dataCadastro, LocalDateTime ultimoLogin) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
        this.nome = nome;
        this.perfil = perfil;
        this.papeis = new HashSet<>(papeis);
        this.saldo = saldo;
        this.dataCadastro = dataCadastro;
        this.ultimoLogin = ultimoLogin;
    }

    /**
     * Factory Method para cadastrar um novo usuário.
     * 
     * @param email     E-mail do usuário.
     * @param senhaHash Hash da senha.
     * @param nome      Nome do usuário.
     * @param papel     Papel inicial.
     * @return Nova instância de Usuario.
     */
    public static Usuario cadastrar(Email email, SenhaHash senhaHash, String nome, Papel papel) {
        return new Usuario(UsuarioId.novo(), email, senhaHash, nome, papel);
    }

    /**
     * Factory Method para reconstituição a partir do banco de dados.
     */
    public static Usuario reconstruir(UsuarioId id, Email email, SenhaHash senhaHash, String nome,
            PerfilUsuario perfil, Set<Papel> papeis, SaldoGamificacao saldo,
            LocalDateTime dataCadastro, LocalDateTime ultimoLogin) {
        return new Usuario(id, email, senhaHash, nome, perfil, papeis, saldo, dataCadastro, ultimoLogin);
    }

    // --- Comportamentos de Negócio ---

    /**
     * Atualiza o perfil público do usuário.
     */
    public void atualizarPerfil(PerfilUsuario novoPerfil) {
        this.perfil = novoPerfil;
    }

    /**
     * Altera a senha do usuário.
     */
    public void alterarSenha(SenhaHash novaSenhaHash) {
        this.senhaHash = novaSenhaHash;
    }

    /**
     * Credita pontos e moedas ao saldo de gamificação.
     */
    public void creditarGamificacao(SaldoGamificacao credito) {
        this.saldo = this.saldo.creditarPontos(credito.pontos())
                .creditarMoedas(credito.moedas());
    }

    /**
     * Debita pontos e moedas do saldo de gamificação.
     */
    public void debitarGamificacao(SaldoGamificacao debito) {
        this.saldo = this.saldo.debitarPontos(debito.pontos())
                .debitarMoedas(debito.moedas());
    }

    /**
     * Adiciona um novo papel ao usuário.
     */
    public void adicionarPapel(Papel papel) {
        this.papeis.add(papel);
    }

    /**
     * Remove um papel do usuário, garantindo que ele mantenha pelo menos um.
     */
    public void removerPapel(Papel papel) {
        if (this.papeis.size() <= 1) {
            throw new IllegalStateException("Usuário deve ter pelo menos um papel");
        }
        this.papeis.remove(papel);
    }

    /**
     * Registra o último login do usuário.
     */
    public void registrarLogin() {
        this.ultimoLogin = LocalDateTime.now();
    }

    // --- Getters ---

    public boolean temPapel(Papel papel) {
        return this.papeis.contains(papel);
    }

    public boolean isProfessor() {
        return temPapel(Papel.ROLE_PROFESSOR);
    }

    public boolean isAdmin() {
        return temPapel(Papel.ROLE_ADMIN);
    }

    public UsuarioId id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public SenhaHash senhaHash() {
        return senhaHash;
    }

    public String nome() {
        return nome;
    }

    public PerfilUsuario perfil() {
        return perfil;
    }

    public Set<Papel> papeis() {
        return Collections.unmodifiableSet(papeis);
    }

    public SaldoGamificacao saldo() {
        return saldo;
    }

    public LocalDateTime dataCadastro() {
        return dataCadastro;
    }

    public LocalDateTime ultimoLogin() {
        return ultimoLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email=" + email +
                ", nome='" + nome + '\'' +
                ", papeis=" + papeis +
                ", saldo=" + saldo +
                '}';
    }
}