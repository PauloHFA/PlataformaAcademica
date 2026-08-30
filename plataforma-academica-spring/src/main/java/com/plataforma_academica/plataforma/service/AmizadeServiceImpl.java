package com.plataforma_academica.plataforma.service;

import java.util.UUID;

import com.plataforma_academica.plataforma.dto.AmizadeDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.AmizadeRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço de amizade responsável por gerenciar o ciclo de vida
 * das conexões entre usuários na plataforma.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional.
 * 
 * @see AmizadeService
 * @see docs/domain/social_context.md
 * @see REQ-020 (Gestão de Amizades)
 */
@Service
public class AmizadeServiceImpl implements AmizadeService {

    private final AmizadeRepository amizadeRepository;
    private final UsuarioRepository usuarioRepository;

    public AmizadeServiceImpl(AmizadeRepository amizadeRepository, UsuarioRepository usuarioRepository) {
        this.amizadeRepository = amizadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Envia uma nova solicitação de amizade entre dois usuários.
     * 
     * @param dto DTO contendo os IDs do solicitante e do destinatário.
     * @return A instância de Amizade criada.
     * @throws BadRequestException       se o usuário tentar enviar solicitação para
     *                                   si mesmo ou se já existir uma solicitação.
     * @throws ResourceNotFoundException se algum dos usuários não for encontrado.
     */
    @Override
    @Transactional
    public Amizade enviarSolicitacao(AmizadeDTO dto) {
        Usuario solicitante = usuarioRepository.findById(dto.getSolicitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitante não encontrado"));
        Usuario destinatario = usuarioRepository.findById(dto.getDestinatarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinatário não encontrado"));
        if (solicitante.getId().equals(destinatario.getId())) {
            throw new BadRequestException("Não pode enviar solicitação para si mesmo");
        }
        Amizade amizade = new Amizade();
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.PENDENTE);
        return amizadeRepository.save(amizade);
    }

    @Override
    @Transactional
    public Amizade responderSolicitacao(UUID amizadeId, String acao) {
        Amizade amizade = amizadeRepository.findById(amizadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));
        if (amizade.getStatus() != Amizade.Status.PENDENTE) {
            throw new BadRequestException("Solicitação já respondida");
        }
        if ("aceitar".equalsIgnoreCase(acao)) {
            amizade.setStatus(Amizade.Status.ACEITO);
        } else if ("recusar".equalsIgnoreCase(acao)) {
            amizade.setStatus(Amizade.Status.RECUSADO);
        } else {
            throw new BadRequestException("Ação inválida");
        }
        return amizadeRepository.save(amizade);
    }

    @Override
    public void removerAmizade(UUID amizadeId) {
        Amizade amizade = amizadeRepository.findById(amizadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Amizade não encontrada"));
        amizadeRepository.delete(amizade);
    }

    @Override
    public List<Amizade> listarSolicitacoesPendentes(UUID usuarioId) {
        return amizadeRepository.findAmizades(
                usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado")),
                Amizade.Status.PENDENTE);
    }

    @Override
    public List<Amizade> listarAmigos(UUID usuarioId) {
        return amizadeRepository.findAmizades(
                usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado")),
                Amizade.Status.ACEITO);
    }
}
