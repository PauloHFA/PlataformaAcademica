package com.plataforma_academica.plataforma.identity.domain.model;

import java.util.Objects;

/**
 * Value Object que agrupa a apresentação pública do usuário.
 * Imutável.
 */
public final class PerfilUsuario {
    private final String fotoUrl;
    private final String biografia;
    private final String instituicao;
    private final String cep;
    private final String site;

    private PerfilUsuario(String fotoUrl, String biografia, String instituicao, String cep, String site) {
        this.fotoUrl = fotoUrl;
        this.biografia = biografia;
        this.instituicao = instituicao;
        this.cep = cep;
        this.site = site;
    }

    public static PerfilUsuario criar(String fotoUrl, String biografia, String instituicao, String cep, String site) {
        return new PerfilUsuario(fotoUrl, biografia, instituicao, cep, site);
    }

    public static PerfilUsuario vazio() {
        return new PerfilUsuario(null, null, null, null, null);
    }

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

    public PerfilUsuario comFotoUrl(String fotoUrl) {
        return new PerfilUsuario(fotoUrl, this.biografia, this.instituicao, this.cep, this.site);
    }

    public PerfilUsuario comBiografia(String biografia) {
        return new PerfilUsuario(this.fotoUrl, biografia, this.instituicao, this.cep, this.site);
    }

    public PerfilUsuario comInstituicao(String instituicao) {
        return new PerfilUsuario(this.fotoUrl, this.biografia, instituicao, this.cep, this.site);
    }

    public PerfilUsuario comCep(String cep) {
        return new PerfilUsuario(this.fotoUrl, this.biografia, this.instituicao, cep, this.site);
    }

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