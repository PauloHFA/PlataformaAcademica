package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Plataforma;

import java.util.List;

public interface PlataformaService {

    Plataforma salvar(Plataforma plataforma);

    Plataforma atualizar(Long id, Plataforma plataforma);

    Plataforma buscarPorId(Long id);

    List<Plataforma> listarTudo();

    void deletar(Long id);
}
