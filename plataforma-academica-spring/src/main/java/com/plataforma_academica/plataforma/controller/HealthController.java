package com.plataforma_academica.plataforma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Fornece endpoints para verificar a saúde da aplicação
 */
@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "http://localhost:4200")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    /**
     * Health check básico da aplicação
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "Plataforma Acadêmica");
        health.put("timestamp", System.currentTimeMillis());
        
        System.out.println("[GET /api/health] Status: UP");
        return ResponseEntity.ok(health);
    }

    /**
     * Verifica conexão com banco de dados
     */
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            boolean isValid = conn.isValid(2);
            dbHealth.put("status", isValid ? "UP" : "DOWN");
            dbHealth.put("database", "PostgreSQL");
            dbHealth.put("valid", isValid);
            
            System.out.println("[GET /api/health/db] Database: " + (isValid ? "UP" : "DOWN"));
            return ResponseEntity.ok(dbHealth);
        } catch (Exception e) {
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
            
            System.out.println("[GET /api/health/db] Database: DOWN - " + e.getMessage());
            return ResponseEntity.status(503).body(dbHealth);
        }
    }

    /**
     * Informações detalhadas da aplicação
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Plataforma Acadêmica");
        info.put("version", "1.0.0");
        info.put("description", "Sistema de gestão acadêmica");
        info.put("java.version", System.getProperty("java.version"));
        info.put("spring.version", org.springframework.boot.SpringBootVersion.getVersion());
        
        System.out.println("[GET /api/health/info] Informações retornadas");
        return ResponseEntity.ok(info);
    }

    /**
     * Verifica uso de memória
     */
    @GetMapping("/memory")
    public ResponseEntity<Map<String, Object>> memoryHealth() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> memory = new HashMap<>();
        
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        memory.put("max", formatBytes(maxMemory));
        memory.put("total", formatBytes(totalMemory));
        memory.put("used", formatBytes(usedMemory));
        memory.put("free", formatBytes(freeMemory));
        memory.put("usage_percent", (usedMemory * 100) / totalMemory);
        
        System.out.println("[GET /api/health/memory] Uso: " + memory.get("usage_percent") + "%");
        return ResponseEntity.ok(memory);
    }

    private String formatBytes(long bytes) {
        long mb = bytes / (1024 * 1024);
        return mb + " MB";
    }
}
