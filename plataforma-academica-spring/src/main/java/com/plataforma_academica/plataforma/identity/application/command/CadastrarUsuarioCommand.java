package com.plataforma_academica.plataforma.identity.application.command;

import com.plataforma_academica.plataforma.identity.domain.model.Papel;

/**
 * Comando para cadastro de novo usuário.
 */
public record CadastrarUsuarioCommand(
        String email,
        String senha,
        String nome,
        Papel papel) {
}