package com.plataforma_academica.plataforma.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioService usuarioService;
    private final String frontendUrl;

    public OAuth2LoginSuccessHandler(UsuarioService usuarioService,
            @Value("${app.frontend-url:http://localhost:4200}") String frontendUrl) {
        this.usuarioService = usuarioService;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        String nome = principal.getAttribute("name");

        if (email == null || email.isBlank()) {
            getRedirectStrategy().sendRedirect(request, response,
                    frontendUrl + "/login?oauth=erro&motivo=email-nao-disponivel");
            return;
        }

        Usuario usuario = usuarioService.loginSocial(email, nome);
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String target = UriComponentsBuilder.fromUriString(frontendUrl + "/login")
                .queryParam("oauth", "sucesso")
                .queryParam("provider", provider)
                .queryParam("id", usuario.getId())
                .queryParam("nome", usuario.getNome())
                .queryParam("email", usuario.getEmail())
                .build().encode().toUriString();
        getRedirectStrategy().sendRedirect(request, response, target);
    }
}
