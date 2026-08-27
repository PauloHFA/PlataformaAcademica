package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.PostagemDTO;
import com.plataforma_academica.plataforma.dto.PostagemResponseDTO;
import com.plataforma_academica.plataforma.mapper.PostagemMapper;
import com.plataforma_academica.plataforma.model.Plataforma;
import com.plataforma_academica.plataforma.model.Postagem;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.PlataformaRepository;
import com.plataforma_academica.plataforma.repository.PostagemRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import com.plataforma_academica.plataforma.repository.CurtidaRepository;
import com.plataforma_academica.plataforma.repository.ComentarioRepository;
import com.plataforma_academica.plataforma.model.Curtida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de postagens sociais.
 * 
 * Camada: Application Service
 * Responsabilidades: Criar, listar, atualizar e excluir postagens no
 * ecossistema social.
 */
/**
 * Implementação do serviço de Postagens no feed social.
 * 
 * Camada: Application / Business Service (Social Context)
 * Padrões aplicados: Service Layer, Repository Pattern, Transactional.
 * 
 * @see PostagemService
 * @see docs/domain/social_context.md
 * @see REQ-025 (Publicação no Feed Social)
 */
@Service
public class PostagemServiceImpl implements PostagemService {

    @Autowired
    PostagemRepository postagemRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PlataformaRepository plataformaRepository;

    @Autowired
    AmizadeService amizadeService;

    @Autowired
    CurtidaRepository curtidaRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    // pasta onde as imagens serão salvas (relativa ao diretório de trabalho)
    private final java.nio.file.Path uploadDir = java.nio.file.Paths.get("uploads").toAbsolutePath();

    private void ensureUploadDir() {
        try {
            java.nio.file.Files.createDirectories(uploadDir);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível criar pasta de uploads", e);
        }
    }

    private String storeFile(org.springframework.web.multipart.MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;
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
        // URL pública relativa
        return "/uploads/" + filename;
    }

    @Override
    public PostagemDTO publicar(PostagemDTO dto) {

        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Plataforma plataforma = null;
        if (dto.getPlataformaId() != null) {
            plataforma = plataformaRepository.findById(dto.getPlataformaId())
                    .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));
        }

        Postagem postagem = PostagemMapper.toEntity(dto, autor, plataforma);
        return PostagemMapper.toDTO(postagemRepository.save(postagem));
    }

    @Override
    public List<PostagemDTO> listarTodas() {
        return postagemRepository.findAll()
                .stream()
                .map(PostagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemDTO buscarPorId(Long id) {
        return postagemRepository.findById(id)
                .map(PostagemMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<PostagemDTO> buscarPorTitulo(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(PostagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemDTO atualizar(PostagemDTO dto) {
        return publicar(dto);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Postagem postagem = postagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
        // Deletar comentários relacionados à postagem
        comentarioRepository.deleteByPostagemId(id);
        // Deletar curtidas relacionadas à postagem
        curtidaRepository.deleteByPostagemId(id);
        // Agora deletar a postagem
        postagemRepository.deleteById(id);
    }

    @Override
    public PostagemResponseDTO publicarResponse(PostagemDTO dto) {
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Plataforma plataforma = null;
        if (dto.getPlataformaId() != null) {
            plataforma = plataformaRepository.findById(dto.getPlataformaId())
                    .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));
        }

        Postagem postagem = PostagemMapper.toEntity(dto, autor, plataforma);
        return PostagemMapper.toResponse(postagemRepository.save(postagem));
    }

    @Override
    public List<PostagemResponseDTO> listarTodasResponse() {
        return postagemRepository.findAll()
                .stream()
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemResponseDTO buscarPorIdResponse(Long id) {
        return postagemRepository.findById(id)
                .map(PostagemMapper::toResponse)
                .orElse(null);
    }

    @Override
    public List<PostagemResponseDTO> buscarPorTituloResponse(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PostagemResponseDTO atualizarResponse(PostagemDTO dto) {
        return publicarResponse(dto);
    }

    @Override
    public PostagemResponseDTO publicarComImagemResponse(PostagemDTO dto,
            org.springframework.web.multipart.MultipartFile imagem) {
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Plataforma plataforma = null;
        if (dto.getPlataformaId() != null) {
            plataforma = plataformaRepository.findById(dto.getPlataformaId())
                    .orElseThrow(() -> new RuntimeException("Plataforma não encontrada"));
        }

        Postagem postagem = PostagemMapper.toEntity(dto, autor, plataforma);

        // salvar imagem se fornecida
        if (imagem != null && !imagem.isEmpty()) {
            String url = storeFile(imagem);
            postagem.setImagemUrl(url);
        }

        return PostagemMapper.toResponse(postagemRepository.save(postagem));
    }

    @Override
    @Transactional
    public PostagemResponseDTO curtir(Long postagemId, Long usuarioId) {
        Postagem postagem = postagemRepository.findById(postagemId)
                .orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        boolean jaCurtiu = curtidaRepository.existsByUsuarioIdAndPostagemId(usuarioId, postagemId);

        if (jaCurtiu) {
            curtidaRepository.deleteByUsuarioIdAndPostagemId(usuarioId, postagemId);
            postagem.setCurtidas(Math.max(0, postagem.getCurtidas() - 1));
        } else {
            Curtida curtida = new Curtida();
            curtida.setUsuario(usuario);
            curtida.setPostagem(postagem);
            curtidaRepository.save(curtida);
            postagem.setCurtidas(postagem.getCurtidas() + 1);
        }

        return PostagemMapper.toResponse(postagemRepository.save(postagem));
    }

    @Override
    public List<PostagemResponseDTO> listarDeAmigos(Long usuarioId) {
        List<Long> amigosIds = amizadeService.listarAmigos(usuarioId)
                .stream()
                .map(amizade -> amizade.getSolicitante().getId().equals(usuarioId)
                        ? amizade.getDestinatario().getId()
                        : amizade.getSolicitante().getId())
                .collect(Collectors.toList());

        return postagemRepository.findAll()
                .stream()
                .filter(p -> amigosIds.contains(p.getAutor().getId()))
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostagemResponseDTO> listarMaisCurtidas() {
        return postagemRepository.findAll()
                .stream()
                .sorted((p1, p2) -> Integer.compare(p2.getCurtidas(), p1.getCurtidas()))
                .limit(10)
                .map(PostagemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean verificarCurtida(Long postagemId, Long usuarioId) {
        return curtidaRepository.existsByUsuarioIdAndPostagemId(usuarioId, postagemId);
    }
}
