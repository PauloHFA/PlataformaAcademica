package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.Usuario;
import java.util.List;
import java.util.Optional; // <-- import necessário

/**
 * Interface do serviço de Usuários.
 * 
 * Camada: Application / Business Service (Identity Context)
 * Padrões aplicados: Service Layer, Repository Pattern, BCrypt.
 * 
 * @see Usuario
 * @see REQ-001 (Autenticação e Perfil de Usuário)
 */
public interface UsuarioService {
    Optional<Usuario> login(String email, String senha);

    Usuario cadastrarUsuario(Usuario usuario);

    Usuario buscarPorId(UUID id);

    List<Usuario> listarTodos();
}
