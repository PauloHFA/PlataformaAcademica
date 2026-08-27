package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;

/**
 * Value Object que agrupa as informações de apresentação pública do usuário.
 * 
 * Este objeto é imutável, garantindo que qualquer alteração em suas
 * propriedades
 * resulte em uma nova instância (padrão Wither).
 * 
 * Padrão aplicado: Value Object (Imutável).
 */
public final class PerfilUsuario {
    private final String fotoUrl;
    private final String biografia;
    private final String instituicao;
    private final String cep;
    private final String site;

    /**
     * Construtor privado para garantir a imutabilidade.
     */
    private PerfilUsuario(String fotoUrl, String biografia, String instituicao, String cep, String site) {
        this.fotoUrl = fotoUrl;
        this.biografia = biografia;
        this.instituicao = instituicao;
        this.cep = cep;
        this.site = site;
    }

    /**
     * Factory Method para criar um perfil com informações completas.
     * 
     * @param fotoUrl     URL da foto de perfil.
     * @param biografia   Biografia ou descrição do usuário.
     * @param instituicao Instituição de ensino ou trabalho.
     * @param cep         Código de Endereçamento Postal.
     * @param site        Website pessoal ou portfólio.
     * @return Nova instância de PerfilUsuario.
     */
    public static PerfilUsuario criar(String fotoUrl, String biografia, String instituicao, String cep, String site) {
        return new PerfilUsuario(fotoUrl, biografia, instituicao, cep, site);
    }

    /**
     * Factory Method para criar um perfil vazio (estado inicial).
     * 
     * @return Instância de PerfilUsuario com campos nulos.
     */
    public static PerfilUsuario vazio() {
        return new PerfilUsuario(null, null, null, null, null);
    }

    // --- Getters ---

    public String fotoUrl() {
        return fotoUrl;
    }

    public String biografia() {
        return biografia;
    }

    public String instituicao() {
        return instituicao;
    }

    public String cep() {
        return cep;
    }

    public String site() {
        return site;
    }

    // --- Padrão Wither (Imutabilidade) ---

    /**
     * Retorna uma nova instância de PerfilUsuario com a foto de perfil atualizada.
     * 
     * @param fotoUrl Nova URL da foto.
     * @return Nova instância de PerfilUsuario.
     */
    public PerfilUsuario comFotoUrl(String fotoUrl) {
        return new PerfilUsuario(fotoUrl, this.biografia, this.instituicao, this.cep, this.site);
    }

    /**
     * Retorna uma nova instância de PerfilUsuario com a biografia atualizada.
     * 
     * @param biografia Nova biografia.
     * @return Nova instância de PerfilUsuario.
     */
    public PerfilUsuario comBiografia(String biografia) {
        return new PerfilUsuario(this.fotoUrl, biografia, this.instituicao, this.cep, this.site);
    }

    /**
     * Retorna uma nova instância de PerfilUsuario com a instituição atualizada.
     * 
     * @param instituicao Nova instituição.
     * @return Nova instância de PerfilUsuario.
     */
    public PerfilUsuario comInstituicao(String instituicao) {
        return new PerfilUsuario(this.fotoUrl, this.biografia, instituicao, this.cep, this.site);
    }

    /**
     * Retorna uma nova instância de PerfilUsuario com o CEP atualizado.
     * 
     * @param cep Novo CEP.
     * @return Nova instância de PerfilUsuario.
     */
    public PerfilUsuario comCep(String cep) {
        return new PerfilUsuario(this.fotoUrl, this.biografia, this.instituicao, cep, this.site);
    }

    /**
     * Retorna uma nova instância de PerfilUsuario com o website atualizado.
     * 
     * @param site Novo website.
     * @return Nova instância de PerfilUsuario.
     */
    public PerfilUsuario comSite(String site) {
        return new PerfilUsuario(this.fotoUrl, this.biografia, this.instituicao, this.cep, site);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PerfilUsuario that = (PerfilUsuario) o;
        return Objects.equals(fotoUrl, that.fotoUrl) &&
                Objects.equals(biografia, that.biografia) &&
                Objects.equals(instituicao, that.instituicao) &&
                Objects.equals(cep, that.cep) &&
                Objects.equals(site, that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fotoUrl, biografia, instituicao, cep, site);
    }

    @Override
    public String toString() {
        return "PerfilUsuario{" +
                "fotoUrl='" + fotoUrl + '\'' +
                ", biografia='" + biografia + '\'' +
                ", instituicao='" + instituicao + '\'' +
                ", cep='" + cep + '\'' +
                ", site='" + site + '\'' +
                '}';
    }
}