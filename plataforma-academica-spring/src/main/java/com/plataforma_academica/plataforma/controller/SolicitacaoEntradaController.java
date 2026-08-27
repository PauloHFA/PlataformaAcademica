package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.SolicitacaoEntrada;
import com.plataforma_academica.plataforma.model.SolicitacaoEntrada.StatusSolicitacao;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.SolicitacaoEntradaRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST responsável pelo gerenciamento de Solicitações de Entrada em
 * Salas de Aula.
 * 
 * Camada: Presentation / REST Controller (Academic Context)
 * Contexto de Negócio: Solicitação, aprovação e rejeição de acesso a salas de
 * aula.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see SolicitacaoEntradaService
 * @see docs/domain/academic_context.md
 * @see REQ-025 (Solicitação e Aprovação de Entrada)
 */
@RestController
@RequestMapping("/api/solicitacoes")
@CrossOrigin(origins = "http://localhost:4200")
public class SolicitacaoEntradaController {

    @Autowired
    private SolicitacaoEntradaRepository solicitacaoRepository;

    @Autowired
    private SaladeAulaRepository salaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/solicitar/{salaId}/{usuarioId}")
    public ResponseEntity<?> solicitarEntrada(@PathVariable Long salaId, @PathVariable Long usuarioId) {
        System.out.println("[POST /api/solicitacoes/solicitar] Sala=" + salaId + ", Usuario=" + usuarioId);

        SaladeAula sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se já existe solicitação pendente
        if (solicitacaoRepository.findBySalaIdAndUsuarioIdAndStatus(salaId, usuarioId, StatusSolicitacao.PENDENTE)
                .isPresent()) {
            return ResponseEntity.badRequest().body("Já existe uma solicitação pendente");
        }

        SolicitacaoEntrada solicitacao = new SolicitacaoEntrada();
        solicitacao.setSala(sala);
        solicitacao.setUsuario(usuario);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);

        SolicitacaoEntrada salva = solicitacaoRepository.save(solicitacao);
        System.out.println("[POST /api/solicitacoes/solicitar] Sucesso: ID=" + salva.getId());
        return ResponseEntity.ok(salva);
    }

    @GetMapping("/sala/{salaId}/pendentes")
    public ResponseEntity<List<SolicitacaoEntrada>> listarPendentes(@PathVariable Long salaId) {
        System.out.println("[GET /api/solicitacoes/sala/" + salaId + "/pendentes]");
        List<SolicitacaoEntrada> pendentes = solicitacaoRepository.findBySalaIdAndStatus(salaId,
                StatusSolicitacao.PENDENTE);
        System.out.println("[GET /api/solicitacoes/sala/" + salaId + "/pendentes] Total: " + pendentes.size());
        return ResponseEntity.ok(pendentes);
    }

    @PutMapping("/{solicitacaoId}/aprovar/{professorId}")
    public ResponseEntity<?> aprovar(@PathVariable Long solicitacaoId, @PathVariable Long professorId) {
        System.out.println("[PUT /api/solicitacoes/" + solicitacaoId + "/aprovar] Professor=" + professorId);

        SolicitacaoEntrada solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        SaladeAula sala = solicitacao.getSala();
        if (!sala.getCriador().getId().equals(professorId)) {
            return ResponseEntity.status(403).body("Apenas o criador da sala pode aprovar solicitações");
        }

        solicitacao.setStatus(StatusSolicitacao.APROVADA);
        solicitacao.setDataResposta(LocalDateTime.now());
        solicitacaoRepository.save(solicitacao);

        // Adiciona usuário à sala
        if (!sala.getUsuarios().contains(solicitacao.getUsuario())) {
            sala.getUsuarios().add(solicitacao.getUsuario());
            salaRepository.save(sala);
        }

        System.out.println("[PUT /api/solicitacoes/" + solicitacaoId + "/aprovar] Sucesso");
        return ResponseEntity.ok(solicitacao);
    }

    @PutMapping("/{solicitacaoId}/rejeitar/{professorId}")
    public ResponseEntity<?> rejeitar(@PathVariable Long solicitacaoId, @PathVariable Long professorId) {
        System.out.println("[PUT /api/solicitacoes/" + solicitacaoId + "/rejeitar] Professor=" + professorId);

        SolicitacaoEntrada solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        SaladeAula sala = solicitacao.getSala();
        if (!sala.getCriador().getId().equals(professorId)) {
            return ResponseEntity.status(403).body("Apenas o criador da sala pode rejeitar solicitações");
        }

        solicitacao.setStatus(StatusSolicitacao.REJEITADA);
        solicitacao.setDataResposta(LocalDateTime.now());
        solicitacaoRepository.save(solicitacao);

        System.out.println("[PUT /api/solicitacoes/" + solicitacaoId + "/rejeitar] Sucesso");
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping("/usuario/{usuarioId}/minhas")
    public ResponseEntity<List<SolicitacaoEntrada>> minhasSolicitacoes(@PathVariable Long usuarioId) {
        System.out.println("[GET /api/solicitacoes/usuario/" + usuarioId + "/minhas]");
        List<SolicitacaoEntrada> solicitacoes = solicitacaoRepository.findByUsuarioIdAndStatus(usuarioId,
                StatusSolicitacao.PENDENTE);
        return ResponseEntity.ok(solicitacoes);
    }
}
