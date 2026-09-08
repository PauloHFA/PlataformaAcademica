package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.UsuarioDTO;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.UsuarioService;
import com.plataforma_academica.plataforma.service.EmailConfirmacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmailConfirmacaoService emailConfirmacaoService;
    private final String frontendUrl;

    public UsuarioController(UsuarioService usuarioService, EmailConfirmacaoService emailConfirmacaoService,
            @Value("${app.frontend-url:http://localhost:4200}") String frontendUrl) {
        this.usuarioService = usuarioService;
        this.emailConfirmacaoService = emailConfirmacaoService;
        this.frontendUrl = frontendUrl;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/buscarporid")
    public ResponseEntity<?> buscarPorParam(@RequestParam(required = false) String id) {
        if (id == null || id.isBlank() || "NaN".equalsIgnoreCase(id) || "null".equalsIgnoreCase(id)
                || "undefined".equalsIgnoreCase(id)) {
            return ResponseEntity.badRequest().body("ID inválido");
        }
        try {
            UUID uuid = UUID.fromString(id.trim());
            Usuario usuario = usuarioService.buscarPorId(uuid);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID inválido: formato UUID esperado");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id) {
        if (id == null || id.isBlank() || "NaN".equalsIgnoreCase(id) || "null".equalsIgnoreCase(id)
                || "undefined".equalsIgnoreCase(id)) {
            return ResponseEntity.badRequest().body("ID inválido");
        }
        try {
            UUID uuid = UUID.fromString(id.trim());
            Usuario usuario = usuarioService.buscarPorId(uuid);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID inválido: formato UUID esperado");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        java.util.Optional<Usuario> resultado = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.status(401).body("Email ou senha incorretos");
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        try {
            Usuario salvo = usuarioService.cadastrarUsuario(usuario);
            emailConfirmacaoService.enviarConfirmacao(salvo);
            return ResponseEntity.ok(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<Void> confirmarEmail(@RequestParam String token) {
        try {
            usuarioService.confirmarEmail(token);
            return ResponseEntity.status(302)
                    .location(URI.create(frontendUrl + "/login?confirmado=true"))
                    .build();
        } catch (IllegalArgumentException erro) {
            return ResponseEntity.status(302)
                    .location(URI.create(frontendUrl + "/login?confirmado=false"))
                    .build();
        }
    }
}
