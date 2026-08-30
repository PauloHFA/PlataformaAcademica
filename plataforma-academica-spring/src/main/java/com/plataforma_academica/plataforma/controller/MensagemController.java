package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.MensagemDTO;
import com.plataforma_academica.plataforma.model.Mensagem;
import com.plataforma_academica.plataforma.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller REST responsável pelo gerenciamento de Mensagens Diretas.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Social / Chat entre usuários.
 * Padrões aplicados: RestController, CrossOrigin, DTOs.
 * 
 * @see MensagemService
 * @see docs/domain/social_context.md
 * @see REQ-035 (Mensagens Diretas)
 */
@RestController
@RequestMapping("/api/mensagens")
@CrossOrigin(origins = "http://localhost:4200")
public class MensagemController {
    @Autowired
    private MensagemRepository mensagemRepository;

    @PostMapping("/enviar")
    public ResponseEntity<Mensagem> enviarMensagem(@Valid @RequestBody MensagemDTO dto) {
        Mensagem mensagem = new Mensagem(dto.getRemetenteId(), dto.getDestinatarioId(), dto.getConteudo());
        Mensagem salva = mensagemRepository.save(mensagem);
        return ResponseEntity.ok(salva);
    }

    @GetMapping("/{usuarioId}/{amigoId}")
    public ResponseEntity<List<Mensagem>> obterMensagens(@PathVariable UUID usuarioId, @PathVariable UUID amigoId) {
        List<Mensagem> mensagens = mensagemRepository.findMensagensEntre(usuarioId, amigoId);
        return ResponseEntity.ok(mensagens);
    }
}
