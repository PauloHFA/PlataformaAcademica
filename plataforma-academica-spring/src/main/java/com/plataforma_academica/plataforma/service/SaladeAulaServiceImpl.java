package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.model.Atividade;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.model.Professor;
import com.plataforma_academica.plataforma.model.SaladeAula;
import com.plataforma_academica.plataforma.repository.AtividadeRepository;
import com.plataforma_academica.plataforma.repository.SaladeAulaRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import com.plataforma_academica.plataforma.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import com.plataforma_academica.plataforma.dto.AtividadeDTO;
import com.plataforma_academica.plataforma.mapper.AtividadeMapper;

@Service
public class SaladeAulaServiceImpl implements SaladeAulaService{

    private final SaladeAulaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AtividadeRepository atividadeRepository;
    private final ProfessorRepository professorRepository;

    public SaladeAulaServiceImpl(
            SaladeAulaRepository salaRepository,
            UsuarioRepository usuarioRepository,
            AtividadeRepository atividadeRepository,
            ProfessorRepository professorRepository) {
        this.salaRepository = salaRepository;
        this.usuarioRepository = usuarioRepository;
        this.atividadeRepository = atividadeRepository;
        this.professorRepository = professorRepository;
    }

    /**
     * Lógica de verificação de permissão: Checa se o usuário é o criador da sala.
     * @param saladeAulaId ID da sala de aula a ser verificada.
     * @param userId ID do usuário que tenta realizar a ação.
     */
    private SaladeAula verificarCriador(Long saladeAulaId, Long userId) {
        // Removido o cast desnecessário (SaladeAula)
        SaladeAula sala = salaRepository.findById(saladeAulaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala de Aula não encontrada com ID: " + saladeAulaId));

        if (!sala.getCriador().getId().equals(userId)) {
            throw new SecurityException("Apenas o criador da sala de aula pode realizar esta operação.");
        }
        return sala;
    }

    // --- Implementações de CRUD para Sala de Aula ---

    @Override
    @Transactional
    public SaladeAula criarSala(SaladeAula sala, Long criadorId) {
        Usuario criador = usuarioRepository.findById(criadorId)
                .orElseThrow(() -> new EntityNotFoundException("Criador não encontrado."));

        if (!(criador instanceof Professor)) {
            throw new SecurityException("Apenas professores podem criar salas de aula.");
        }

        sala.setCriador(criador);
        sala.setCodigoSala(gerarCodigoUnico());

        if (sala.getUsuarios() == null) {
            sala.setUsuarios(new ArrayList<>());
        }

        if (!sala.getUsuarios().contains(criador)) {
            sala.getUsuarios().add(criador);
        }
        return salaRepository.save(sala);
    }
    
    private String gerarCodigoUnico() {
        String codigo;
        do {
            codigo = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (salaRepository.findByCodigoSala(codigo).isPresent());
        return codigo;
    }

    @Override
    public SaladeAula buscarSalaPorId(Long saladeAulaId) {
        // Removido o cast desnecessário (SaladeAula)
        return salaRepository.findById(saladeAulaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala de Aula não encontrada."));
    }

    @Override
    // CORRIGIDO: O tipo de retorno deve ser List<SaladeAula>
    public List<SaladeAula> listarTodasSalas() {
        return salaRepository.findAll();
    }

    @Override
    @Transactional
    public void deletarSala(Long saladeAulaId, Long userId) {
        // Valida se o usuário é o criador antes de deletar
        verificarCriador(saladeAulaId, userId);
        salaRepository.deleteById(saladeAulaId);
    }

    // --- Implementações de Gerenciamento de Membros ---

    @Override
    @Transactional
    public Usuario adicionarMembro(Long saladeAulaId, Long novoMembroId, Long creatorId) {
        // 1. Verifica se o usuário logado é o criador
        SaladeAula sala = verificarCriador(saladeAulaId, creatorId);

        // 2. Busca o novo membro
        Usuario novoMembro = usuarioRepository.findById(novoMembroId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário a ser adicionado não encontrado."));

        // 3. Adiciona e salva
        if (sala.getUsuarios().stream().noneMatch(u -> u.getId().equals(novoMembroId))) {
            sala.getUsuarios().add(novoMembro);
            // CORRIGIDO: Salvando a instância da entidade (sala)
            this.salaRepository.save(sala);
        }
        return novoMembro;
    }

    @Override
    public List<Usuario> listarMembros(Long saladeAulaId) {
        SaladeAula sala = buscarSalaPorId(saladeAulaId);
        return sala.getUsuarios();
    }

    @Override
    @Transactional
    public void removerMembro(Long saladeAulaId, Long membroId, Long creatorId) {
        // 1. Verifica se o usuário logado é o criador
        SaladeAula sala = verificarCriador(saladeAulaId, creatorId);

        // 2. Remove o membro
        boolean removed = sala.getUsuarios().removeIf(u -> u.getId().equals(membroId));

        if (removed) {
            // CORRIGIDO: Salvando a instância da entidade (sala)
            salaRepository.save(sala);
        } else {
            throw new EntityNotFoundException("Membro não encontrado na sala de aula.");
        }
    }

    // --- Implementações de Gerenciamento de Atividades ---

    @Override
    @Transactional
    public Atividade cadastrarAtividade(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId) {
        // 1. Verifica se o usuário logado é o criador
        SaladeAula sala = verificarCriador(saladeAulaId, creatorId);

        // 2. Busca o autor (professor primeiro)
        Usuario autor = professorRepository.findById(creatorId)
                .map(p -> (Usuario) p)
                .orElseGet(() -> usuarioRepository.findById(creatorId)
                        .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado.")));
        
        if (!(autor instanceof Professor)) {
            throw new SecurityException("Apenas professores podem criar atividades.");
        }
        
        // 3. Converte DTO para entidade e define relacionamentos
        Atividade atividade = AtividadeMapper.toEntity(atividadeDTO, autor, sala);

        // 4. Salva a atividade
        return atividadeRepository.save(atividade);
    }

    @Override
    public Atividade buscarAtividadePorId(Long atividadeId) {
        return atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));
    }

    @Override
    public List<Atividade> listarAtividadesPorSala(Long saladeAulaId) {
        // Geralmente implementado usando um método de Repositório: findBySalaDeAulaId(saladeAulaId)
        return atividadeRepository.findAll().stream() // Simulação sem query real
                .filter(a -> a.getSalaDeAula() != null && a.getSalaDeAula().getId().equals(saladeAulaId))
                .toList();
    }

    @Override
    @Transactional
    public Atividade atualizarAtividade(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId) {
        // 1. Verifica se o usuário logado é o criador
        verificarCriador(saladeAulaId, creatorId);

        // 2. Garante que a atividade existe e pertence à sala
        Atividade atividadeExistente = atividadeRepository.findById(atividadeDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada para atualização."));

        if (!atividadeExistente.getSalaDeAula().getId().equals(saladeAulaId)) {
            throw new SecurityException("A Atividade não pertence à Sala especificada.");
        }

        // 3. Atualiza a entidade usando o mapper
        AtividadeMapper.updateEntity(atividadeExistente, atividadeDTO);

        return atividadeRepository.save(atividadeExistente);
    }

    @Override
    @Transactional
    public void deletarAtividade(Long atividadeId, Long creatorId) {
        Atividade atividade = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada."));

        // Garante que o usuário que está deletando é o criador da Sala de Aula à qual a Atividade pertence.
        verificarCriador(atividade.getSalaDeAula().getId(), creatorId);

        atividadeRepository.delete(atividade);
    }
    
    @Override
    @Transactional
    public Atividade cadastrarAtividadeComDocumento(Long saladeAulaId, AtividadeDTO atividadeDTO, Long creatorId, org.springframework.web.multipart.MultipartFile documento) {
        SaladeAula sala = verificarCriador(saladeAulaId, creatorId);
        
        // Busca primeiro no ProfessorRepository
        Usuario autor = professorRepository.findById(creatorId)
                .map(p -> (Usuario) p)
                .orElseGet(() -> usuarioRepository.findById(creatorId)
                        .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado.")));
        
        System.out.println("[DEBUG] Tipo do autor: " + autor.getClass().getName());
        System.out.println("[DEBUG] É Professor? " + (autor instanceof Professor));
        
        if (!(autor instanceof Professor)) {
            throw new SecurityException("Apenas professores podem criar atividades.");
        }
        
        Atividade atividade = AtividadeMapper.toEntity(atividadeDTO, autor, sala);
        
        if (documento != null && !documento.isEmpty()) {
            String url = storeFile(documento);
            atividade.setDocumentoUrl(url);
        }
        
        return atividadeRepository.save(atividade);
    }
    
    private final java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads").toAbsolutePath();
    
    private void ensureUploadDir() {
        try {
            java.nio.file.Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar pasta de uploads", e);
        }
    }
    
    private String storeFile(org.springframework.web.multipart.MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        ensureUploadDir();
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = java.util.UUID.randomUUID().toString() + ext;
        java.nio.file.Path target = uploadDir.resolve(filename);
        try (java.io.InputStream is = file.getInputStream()) {
            java.nio.file.Files.copy(is, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar arquivo", e);
        }
        return "/uploads/" + filename;
    }
}