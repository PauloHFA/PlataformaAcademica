package com.plataforma_academica.plataforma.identity.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado que encapsula o estado e o histórico do relacionamento bidirecional
 * entre dois usuários.
 * Raiz do Agregado (Aggregate Root).
 */
public final class ConexaoAmizade {
    private final ConexaoId id;
    private final UsuarioId solicitanteId;
    private final UsuarioId destinatarioId;
    private StatusAmizade status;
    private final LocalDateTime dataSolicitacao;
    private LocalDateTime dataResposta;

    private ConexaoAmizade(ConexaoId id, UsuarioId solicitanteId, UsuarioId destinatarioId) {
        if (solicitanteId.equals(destinatarioId)) {
            throw new AutoConexaoInvalidaException("Um usuário não pode se conectar a si mesmo");
        }
        this.id = id;
        this.solicitanteId = solicitanteId;
        this.destinatarioId = destinatarioId;
        this.status = StatusAmizade.PENDENTE;
        this.dataSolicitacao = LocalDateTime.now();
        this.dataResposta = null;
    }

    private ConexaoAmizade(ConexaoId id, UsuarioId solicitanteId, UsuarioId destinatarioId, StatusAmizade status,
            LocalDateTime dataSolicitacao, LocalDateTime dataResposta) {
        this.id = id;
        this.solicitanteId = solicitanteId;
        this.destinatarioId = destinatarioId;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
        this.dataResposta = dataResposta;
    }

    public static ConexaoAmizade solicitar(UsuarioId solicitanteId, UsuarioId destinatarioId) {
        return new ConexaoAmizade(ConexaoId.novo(), solicitanteId, destinatarioId);
    }

    // Construtor para reconstrução a partir do banco de dados
    public static ConexaoAmizade reconstituir(ConexaoId id, UsuarioId solicitanteId, UsuarioId destinatarioId,
            StatusAmizade status, LocalDateTime dataSolicitacao, LocalDateTime dataResposta) {
        return new ConexaoAmizade(id, solicitanteId, destinatarioId, status, dataSolicitacao, dataResposta);
    }

    public void aceitar(UsuarioId destinatario) {
        if (!this.destinatarioId.equals(destinatario)) {
            throw new IllegalArgumentException("Apenas o destinatário pode aceitar a solicitação");
        }
        if (this.status != StatusAmizade.PENDENTE) {
            throw new IllegalStateException("Solicitação não está pendente: " + this.status);
        }
        this.status = StatusAmizade.ACEITO;
        this.dataResposta = LocalDateTime.now();
    }

    public void recusar(UsuarioId destinatario) {
        if (!this.destinatarioId.equals(destinatario)) {
            throw new IllegalArgumentException("Apenas o destinatário pode recusar a solicitação");
        }
        if (this.status != StatusAmizade.PENDENTE) {
            throw new IllegalStateException("Solicitação não está pendente: " + this.status);
        }
        this.status = StatusAmizade.RECUSADO;
        this.dataResposta = LocalDateTime.now();
    }

    public void bloquear(UsuarioId solicitanteOuDestinatario) {
        if (!this.solicitanteId.equals(solicitanteOuDestinatario) &&
                !this.destinatarioId.equals(solicitanteOuDestinatario)) {
            throw new IllegalArgumentException("Apenas os participantes podem bloquear a conexão");
        }
        this.status = StatusAmizade.BLOQUEADO;
        this.dataResposta = LocalDateTime.now();
    }

    public boolean isAceito() {
        return this.status == StatusAmizade.ACEITO;
    }

    public boolean isPendente() {
        return this.status == StatusAmizade.PENDENTE;
    }

    public boolean envolveUsuario(UsuarioId usuarioId) {
        return this.solicitanteId.equals(usuarioId) || this.destinatarioId.equals(usuarioId);
    }

    public ConexaoId id() {
        return id;
    }

    public UsuarioId solicitanteId() {
        return solicitanteId;
    }

    public UsuarioId destinatarioId() {
        return destinatarioId;
    }

    public StatusAmizade status() {
        return status;
    }

    public LocalDateTime dataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime dataResposta() {
        return dataResposta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConexaoAmizade that = (ConexaoAmizade) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ConexaoAmizade{" +
                "id=" + id +
                ", solicitanteId=" + solicitanteId +
                ", destinatarioId=" + destinatarioId +
                ", status=" + status +
                ", dataSolicitacao=" + dataSolicitacao +
                ", dataResposta=" + dataResposta +
                '}';
    }
}