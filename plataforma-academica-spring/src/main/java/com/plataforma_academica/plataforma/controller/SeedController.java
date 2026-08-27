package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.model.*;
import com.plataforma_academica.plataforma.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final UsuarioRepository usuarioRepository;
    private final PostagemRepository postagemRepository;
    private final ComunidadeRepository comunidadeRepository;
    private final ArtigoRepository artigoRepository;
    private final SaladeAulaRepository saladeAulaRepository;

    public SeedController(UsuarioRepository usuarioRepository,
            PostagemRepository postagemRepository,
            ComunidadeRepository comunidadeRepository,
            ArtigoRepository artigoRepository,
            SaladeAulaRepository saladeAulaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.postagemRepository = postagemRepository;
        this.comunidadeRepository = comunidadeRepository;
        this.artigoRepository = artigoRepository;
        this.saladeAulaRepository = saladeAulaRepository;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<String> popularDados() {
        if (usuarioRepository.count() > 0) {
            return ResponseEntity.ok("Banco já possui dados cadastrados!");
        }

        // 1. Usuários
        Usuario ana = new Usuario();
        ana.setNome("Ana Clara");
        ana.setSobrenome("Silva");
        ana.setEmail("ana@academica.edu");
        ana.setSenha("senha123");
        ana.setDescricao("Estudante de Engenharia");
        ana.setInstituicaoEnsino("UFRJ");
        ana.setCidade("Rio de Janeiro");
        ana.setPais("Brasil");
        usuarioRepository.save(ana);

        Usuario bruno = new Usuario();
        bruno.setNome("Bruno Lima");
        bruno.setSobrenome("Lima");
        bruno.setEmail("bruno@academica.edu");
        bruno.setSenha("senha123");
        bruno.setDescricao("Pesquisador");
        bruno.setInstituicaoEnsino("USP");
        bruno.setCidade("São Paulo");
        bruno.setPais("Brasil");
        usuarioRepository.save(bruno);

        Professor carlos = new Professor();
        carlos.setNome("Carlos Mendes");
        carlos.setSobrenome("Mendes");
        carlos.setEmail("carlos@academica.edu");
        carlos.setSenha("senha123");
        carlos.setDescricao("Professor de Matemática");
        carlos.setInstituicaoEnsino("UNICAMP");
        carlos.setCidade("Campinas");
        carlos.setPais("Brasil");
        carlos.setMatricula("PROF-001");
        usuarioRepository.save(carlos);

        // 2. Postagens
        Postagem p1 = new Postagem();
        p1.setTitulo("Bem-vindo à plataforma!");
        p1.setConteudo("Este é um exemplo de postagem ilustrativa para o feed acadêmico.");
        p1.setAutor(ana);
        postagemRepository.save(p1);

        // 3. Comunidades
        Comunidade com = new Comunidade();
        com.setNome("Comunidade de Matemática");
        com.setDescricao("Espaço para discutir problemas de cálculo e álgebra.");
        comunidadeRepository.save(com);

        // 4. Artigos
        Artigo art = new Artigo();
        art.setTitulo("Introdução ao Cálculo Vetorial");
        art.setConteudo("Artigo ilustrativo sobre vetores e aplicações.");
        art.setAutor(carlos);
        artigoRepository.save(art);

        return ResponseEntity.ok("Dados ilustrativos inseridos com sucesso via endpoint /api/seed!");
    }
}
