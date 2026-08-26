package com.plataforma_academica.plataforma.identity.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa o saldo de gamificação do usuário.
 * Garante não-negatividade de pontos e moedas.
 * Imutável.
 */
public final class SaldoGamificacao {
    private final BigDecimal pontos;
    private final BigDecimal moedas;
    private final String nivel;

    private SaldoGamificacao(BigDecimal pontos, BigDecimal moedas, String nivel) {
        if (pontos == null || pontos.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Pontos não podem ser negativos");
        }
        if (moedas == null || moedas.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Moedas não podem ser negativas");
        }
        this.pontos = pontos;
        this.moedas = moedas;
        this.nivel = nivel != null ? nivel : "Bronze";
    }

    public static SaldoGamificacao zerado() {
        return new SaldoGamificacao(BigDecimal.ZERO, BigDecimal.ZERO, "Bronze");
    }

    public static SaldoGamificacao de(BigDecimal pontos, BigDecimal moedas, String nivel) {
        return new SaldoGamificacao(pontos, moedas, nivel);
    }

    public SaldoGamificacao creditarPontos(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        return new SaldoGamificacao(this.pontos.add(quantidade), this.moedas, this.nivel);
    }

    public SaldoGamificacao debitarPontos(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        BigDecimal novoSaldo = this.pontos.subtract(quantidade);
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente de pontos");
        }
        return new SaldoGamificacao(novoSaldo, this.moedas, this.nivel);
    }

    public SaldoGamificacao creditarMoedas(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        return new SaldoGamificacao(this.pontos, this.moedas.add(quantidade), this.nivel);
    }

    public SaldoGamificacao debitarMoedas(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        BigDecimal novoSaldo = this.moedas.subtract(quantidade);
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente de moedas");
        }
        return new SaldoGamificacao(this.pontos, novoSaldo, this.nivel);
    }

    public SaldoGamificacao comNivel(String nivel) {
        return new SaldoGamificacao(this.pontos, this.moedas, nivel);
    }

    public BigDecimal pontos() {
        return pontos;
    }

    public BigDecimal moedas() {
        return moedas;
    }

    public String nivel() {
        return nivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SaldoGamificacao that = (SaldoGamificacao) o;
        return Objects.equals(pontos, that.pontos) &&
                Objects.equals(moedas, that.moedas) &&
                Objects.equals(nivel, that.nivel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pontos, moedas, nivel);
    }

    @Override
    public String toString() {
        return "SaldoGamificacao{" +
                "pontos=" + pontos +
                ", moedas=" + moedas +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}