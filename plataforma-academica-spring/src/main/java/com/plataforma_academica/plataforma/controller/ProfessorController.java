package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import com.plataforma_academica.plataforma.model.Professor;
import com.plataforma_academica.plataforma.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller REST responsável por Professores.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Identity / Listagem e operações de docentes.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see Professor
 * @see REQ-005 (Gestão de Papéis Docentes)
 */
@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Professor professor) {
        System.out.println("[POST /api/professores/cadastro] Recebido: email=" + professor.getEmail() + ", matricula="
                + professor.getMatricula());

        if (professorRepository.findByEmail(professor.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        professor.setSenha(passwordEncoder.encode(professor.getSenha()));
        Professor novoProfessor = professorRepository.save(professor);

        System.out.println("[POST /api/professores/cadastro] Sucesso: ID=" + novoProfessor.getId());
        return ResponseEntity.ok(novoProfessor);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Professor professor) {
        System.out.println("[POST /api/professores/login] Tentativa: email=" + professor.getEmail());

        Optional<Professor> professorOpt = professorRepository.findByEmail(professor.getEmail());

        if (professorOpt.isPresent() && passwordEncoder.matches(professor.getSenha(), professorOpt.get().getSenha())) {
            Professor prof = professorOpt.get();
            System.out.println(
                    "[POST /api/professores/login] Sucesso: ID=" + prof.getId() + ", Matricula=" + prof.getMatricula());
            return ResponseEntity.ok(prof);
        }

        System.out.println("[POST /api/professores/login] Falha: credenciais inválidas");
        return ResponseEntity.status(401).body("Email ou senha incorretos");
    }
}
