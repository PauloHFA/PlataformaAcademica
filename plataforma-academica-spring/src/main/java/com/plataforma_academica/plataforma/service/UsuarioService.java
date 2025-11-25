package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Usuario;
import java.util.List;
import java.util.Optional;    // <-- import necessário

public interface UsuarioService {
    Optional<Usuario> login(String email, String senha);
    Usuario cadastrarUsuario(Usuario usuario);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
}
