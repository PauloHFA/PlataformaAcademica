package com.plataforma_academica.plataforma.service;

import com.plataforma_academica.plataforma.dto.ArtigoDTO;
import com.plataforma_academica.plataforma.exception.BadRequestException;
import com.plataforma_academica.plataforma.exception.ResourceNotFoundException;
import com.plataforma_academica.plataforma.model.Artigo;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.ArtigoRepository;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação do serviço de artigos acadêmicos.
 * 
 * Camada: Application Service
 * Responsabilidades: Criar, editar, listar e excluir artigos, garantindo
 * que apenas o autor possa modificar seu conteúdo.
 */
@Service
public class ArtigoServiceImpl implements ArtigoService {

    private final ArtigoRepository artigoRepo;
    private final UsuarioRepository usuarioRepo;

    public ArtigoServiceImpl(ArtigoRepository artigoRepo, UsuarioRepository usuarioRepo) {
        this.artigoRepo = artigoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Cria um novo artigo acadêmico associado a um autor.
     * 
     * @param dto DTO contendo título, conteúdo e ID do autor.
     * @return Artigo criado e persistido.
     * @throws ResourceNotFoundException se o autor não for encontrado.
     */
    @Override
    public Artigo criar(ArtigoDTO dto) {
        // Passo 1: Recupera o autor pelo ID
        Usuario autor = usuarioRepo.findById(dto.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

        // Passo 2: Cria a entidade Artigo com os dados do DTO
        Artigo artigo = new Artigo();
        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());
        artigo.setAutor(autor);

        // Passo 3: Persiste o artigo
        return artigoRepo.save(artigo);
    }

    /**
     * Edita um artigo existente, validando que apenas o autor pode modificar.
     * 
     * @param id ID do artigo a ser editado.
     * @param dto DTO com novos dados.
     * @return Artigo atualizado.
     * @throws ResourceNotFoundException se o artigo não for encontrado.
     * @throws BadRequestException se o usuário que edita não for o autor.
     */
    @Override
    @Transactional
    public Artigo editar(Long id, ArtigoDTO dto) {
        // Passo 1: Recupera o artigo existente
        Artigo artigo = artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        // Passo 2: Valida se o usuário que edita é o autor original
        if (!artigo.getAutor().getId().equals(dto.getAutorId())) {
            throw new BadRequestException("Apenas o autor pode editar o artigo.");
        }

        // Passo 3: Atualiza os campos permitidos
        artigo.setTitulo(dto.getTitulo());
        artigo.setConteudo(dto.getConteudo());
        artigo.setAtualizadoEm(LocalDateTime.now());

        return artigoRepo.save(artigo);
    }tualizadoEm(LocalDateTime.now());

        return artigoRepo.save(artigo);
    }

    @Override
    public void deletar(Long id, Long solicitanteId) {
        Artigo artigo = artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));

        if (!artigo.getAutor().getId().equals(solicitanteId)) {
            throw new BadRequestException("Apenas o autor pode deletar o artigo.");
        }

        artigoRepo.delete(artigo);
    }

    @Override
    public Artigo buscarPorId(Long id) {
        return artigoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artigo não encontrado"));
    }

    @Override
    public List<Artigo> listarTodos() {
        return artigoRepo.findAll();
    }

    @Override
    public List<Artigo> listarPorAutor(Long autorId) {
        return artigoRepo.findByAutorId(autorId);
    }
}
