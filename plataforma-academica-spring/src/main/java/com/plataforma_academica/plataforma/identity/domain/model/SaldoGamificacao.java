package com.plataforma_academica.plataforma.identity.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa o saldo de gamificação do usuário.
 * 
 * Encapsula pontos, moedas e nível, garantindo invariantes de não-negatividade
 * e regras de negócio para crédito/débito.
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public final class SaldoGamificacao {
    private final BigDecimal pontos;
    private final BigDecimal moedas;
    private final String nivel;

    /**
     * Construtor privado com validação de invariantes.
     * 
     * @param pontos Pontos de experiência (não-negativo).
     * @param moedas Moedas virtuais (não-negativo).
     * @param nivel  Nível atual do usuário (ex: Bronze, Prata, Ouro).
     * @throws IllegalArgumentException se pontos ou moedas forem negativos.
     */
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

    /**
     * Factory Method para criar um saldo zerado (estado inicial).
     * 
     * @return Instância com 0 pontos, 0 moedas e nível Bronze.
     */
    public static SaldoGamificacao zerado() {
        return new SaldoGamificacao(BigDecimal.ZERO, BigDecimal.ZERO, "Bronze");
    }

    /**
     * Factory Method para criar um saldo com valores específicos.
     * 
     * @param pontos Pontos de experiência.
     * @param moedas Moedas virtuais.
     * @param nivel  Nível do usuário.
     * @return Nova instância de SaldoGamificacao.
     */
    public static SaldoGamificacao de(BigDecimal pontos, BigDecimal moedas, String nivel) {
        return new SaldoGamificacao(pontos, moedas, nivel);
    }

    /**
     * Credita pontos ao saldo, retornando nova instância imutável.
     * 
     * @param quantidade Quantidade de pontos a creditar (deve ser positiva).
     * @return Nova instância com pontos atualizados.
     * @throws IllegalArgumentException se quantidade for nula ou não-positiva.
     */
    public SaldoGamificacao creditarPontos(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        return new SaldoGamificacao(this.pontos.add(quantidade), this.moedas, this.nivel);
    }

    /**
     * Debita pontos do saldo, retornando nova instância imutável.
     * 
     * @param quantidade Quantidade de pontos a debitar (deve ser positiva).
     * @return Nova instância com pontos atualizados.
     * @throws IllegalArgumentException se quantidade for nula ou não-positiva.
     * @throws IllegalStateException    se o saldo resultante for negativo.
     */
    public SaldoGamificacao debitarPontos(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        BigDecimal novoSaldo = this.pontos.subtract(quantidade);
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Saldo insuficiente para débito");
        }
        return new SaldoGamificacao(novoSaldo, this.moedas, this.nivel);
    }

    /**
     * Credita moedas ao saldo, retornando nova instância imutável.
     * 
     * @param quantidade Quantidade de moedas a creditar (deve ser positiva).
     * @return Nova instância com moedas atualizadas.
     * @throws IllegalArgumentException se quantidade for nula ou não-positiva.
     */
    public SaldoGamificacao creditarMoedas(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        return new SaldoGamificacao(this.pontos, this.moedas.add(quantidade), this.nivel);
    }

    /**
     * Debita moedas do saldo, retornando nova instância imutável.
     * 
     * @param quantidade Quantidade de moedas a debitar (deve ser positiva).
     * @return Nova instância com moedas atualizadas.
     * @throws IllegalArgumentException se quantidade for nula ou não-positiva.
     * @throws IllegalStateException    se o saldo resultante for negativo.
     */
    public SaldoGamificacao debitarMoedas(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        BigDecimal novoSaldo = this.moedas.subtract(quantidade);
        if (novoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Saldo insuficiente para débito");
        }
        return new SaldoGamificacao(this.pontos, novoSaldo, this.nivel);
    }

    /**
     * Atualiza o nível do usuário, retornando nova instância imutável.
     * 
     * @param novoNivel Novo nível (ex: Bronze, Prata, Ouro, Diamante).
     * @return Nova instância com nível atualizado.
     */
    public SaldoGamificacao comNivel(String novoNivel) {
        return new SaldoGamificacao(this.pontos, this.moedas, novoNivel);
    }

    // --- Getters ---

    public BigDecimal pontos() {
        return pontos;
    }

    public BigDecimal moedas() {
        return moedas;
    }

    public String nivel() {
        return nivel;
    }rgumentException("Saldo insuficiente de pontos");
        }return new SaldoGamificacao(novoSaldo,this.moedas,this.nivel);

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