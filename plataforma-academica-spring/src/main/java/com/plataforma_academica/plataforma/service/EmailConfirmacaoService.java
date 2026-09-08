package com.plataforma_academica.plataforma.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.plataforma_academica.plataforma.model.Usuario;

@Service
public class EmailConfirmacaoService {

    private final JavaMailSender mailSender;
    private final String frontendUrl;
    private final String remetente;

    public EmailConfirmacaoService(
            JavaMailSender mailSender,
            @Value("${app.frontend-url:http://localhost:4200}") String frontendUrl,
            @Value("${spring.mail.username:no-reply@plataforma-academica.local}") String remetente) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
        this.remetente = remetente;
    }

    public void enviarConfirmacao(Usuario usuario) {
        if (usuario.getTokenConfirmacaoEmail() == null) {
            return;
        }

        String link = frontendUrl + "/confirmar-email?token=" + usuario.getTokenConfirmacaoEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remetente);
        message.setTo(usuario.getEmail());
        message.setSubject("Confirme seu e-mail | Plataforma Acadêmica");
        message.setText("Olá, " + usuario.getNome() + "!\n\n"
                + "Confirme seu e-mail para ativar sua conta na Plataforma Acadêmica:\n\n"
                + link + "\n\n"
                + "Este link expira em 24 horas.\n\n"
                + "Se você não criou esta conta, ignore esta mensagem.");
        mailSender.send(message);
    }
}
