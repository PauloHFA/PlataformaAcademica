package com.plataforma_academica.plataforma.service;

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

@Service
public class AmizadeServiceImpl implements AmizadeService {

    private final AmizadeRepository amizadeRepository;
    private final UsuarioRepository usuarioRepository;

    public AmizadeServiceImpl(AmizadeRepository amizadeRepository, UsuarioRepository usuarioRepository) {
        this.amizadeRepository = amizadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Amizade enviarSolicitacao(AmizadeDTO dto) {

        if (dto.getSolicitanteId().equals(dto.getDestinatarioId()))
            throw new BadRequestException("Não é possível enviar solicitação para si mesmo.");

        Usuario solicitante = usuarioRepository.findById(dto.getSolicitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitante não encontrado"));

        Usuario destinatario = usuarioRepository.findById(dto.getDestinatarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinatário não encontrado"));

        // verificar se já existe solicitação
        amizadeRepository.findBySolicitanteIdAndDestinatarioId(
                solicitante.getId(), destinatario.getId()
        ).ifPresent(a -> {
            throw new BadRequestException("Solicitação já existe.");
        });

        Amizade amizade = new Amizade();
        amizade.setSolicitante(solicitante);
        amizade.setDestinatario(destinatario);
        amizade.setStatus(Amizade.Status.PENDENTE);

        return amizadeRepository.save(amizade);
    }

    @Override
    @Transactional
    public Amizade responderSolicitacao(Long amizadeId, String acao) {

        Amizade amizade = amizadeRepository.findById(amizadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada"));

        if (amizade.getStatus() != Amizade.Status.PENDENTE)
            throw new BadRequestException("Solicitação já foi respondida.");

        if ("aceitar".equalsIgnoreCase(acao)) {
            amizade.setStatus(Amizade.Status.ACEITO);
        }
        else if ("recusar".equalsIgnoreCase(acao)) {
            amizade.setStatus(Amizade.Status.RECUSADO);
        }
        else {
            throw new BadRequestException("Ação inválida. Use 'aceitar' ou 'recusar'.");
        }

        return amizadeRepository.save(amizade);
    }

    @Override
    public void removerAmizade(Long amizadeId) {
        Amizade amizade = amizadeRepository.findById(amizadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Amizade não encontrada"));

        amizadeRepository.delete(amizade);
    }

    @Override
    public List<Amizade> listarSolicitacoesPendentes(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return amizadeRepository.findSolicitacoesPendentes(usuarioId);
    }

    @Override
    public List<Amizade> listarAmigos(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return amizadeRepository.findAmigosAceitos(usuarioId);
    }
}
