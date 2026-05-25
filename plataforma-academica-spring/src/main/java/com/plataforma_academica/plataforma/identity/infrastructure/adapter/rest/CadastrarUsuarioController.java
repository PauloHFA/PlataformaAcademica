package com.plataforma_academica.plataforma.identity.infrastructure.adapter.rest;

import com.plataforma_academica.plataforma.identity.application.command.CadastrarUsuarioCommand;
import com.plataforma_academica.plataforma.identity.application.command.CadastrarUsuarioCommandHandler;
import com.plataforma_academica.plataforma.identity.domain.model.Papel;
import com.plataforma_academica.plataforma.identity.domain.model.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity/usuarios")
public class CadastrarUsuarioController {

    private final CadastrarUsuarioCommandHandler commandHandler;

    public CadastrarUsuarioController(CadastrarUsuarioCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastrarUsuarioRequest request) {
        CadastrarUsuarioCommand command = new CadastrarUsuarioCommand(
                request.email(),
                request.senha(),
                request.nome(),
                request.papel());

        Usuario usuario = commandHandler.handle(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromDomain(usuario));
    }

    public record CadastrarUsuarioRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6) String senha,
            @NotBlank String nome,
            @NotNull Papel papel) {
    }

    public record UsuarioResponse(
            String id,
            String email,
            String nome,
            String papel) {
        public static UsuarioResponse fromDomain(Usuario usuario) {
            return new UsuarioResponse(
                    usuario.id().valor().toString(),
                    usuario.email().endereco(),
                    usuario.nome(),
                    usuario.papeis().iterator().next().name());
        }
    }
}
