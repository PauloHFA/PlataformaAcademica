package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.DashboardAlunoDTO;
import com.plataforma_academica.plataforma.service.DashboardAlunoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Controller responsável pelo fornecimento de dados para dashboards acadêmicos.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Métricas de desempenho e resumos de atividades para
 * alunos e salas de aula.
 * Padrões aplicados: RestController, CrossOrigin.
 * 
 * @see DashboardAlunoService
 * @see REQ-030 (Dashboard Acadêmico)
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardAlunoService dashboardAlunoService;

    public DashboardController(DashboardAlunoService dashboardAlunoService) {
        this.dashboardAlunoService = dashboardAlunoService;
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<?> getDashboardAluno(
            @PathVariable Long alunoId,
            @RequestParam Long salaId,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            LocalDate dataInicio = inicio != null ? LocalDate.parse(inicio) : null;
            LocalDate dataFim = fim != null ? LocalDate.parse(fim) : null;

            DashboardAlunoDTO dashboard = dashboardAlunoService.obterDashboardAluno(alunoId, salaId, dataInicio,
                    dataFim);
            return ResponseEntity.ok(dashboard);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Formato de data inválido. Use yyyy-MM-dd.");
        }
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<?> getDashboardSala(
            @PathVariable Long salaId,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        try {
            LocalDate dataInicio = inicio != null ? LocalDate.parse(inicio) : null;
            LocalDate dataFim = fim != null ? LocalDate.parse(fim) : null;

            return ResponseEntity.ok(dashboardAlunoService.obterDashboardSala(salaId, dataInicio, dataFim));
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body("Formato de data inválido. Use yyyy-MM-dd.");
        }
    }
}
