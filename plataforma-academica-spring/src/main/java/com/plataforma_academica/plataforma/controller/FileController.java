package com.plataforma_academica.plataforma.controller;

import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller REST responsável por upload e download de arquivos.
 * 
 * Camada: Presentation / REST Controller
 * Contexto de Negócio: Generic / Armazenamento de anexos da plataforma.
 * Padrões aplicados: RestController, CrossOrigin, MultipartFile.
 * 
 * @see docs/architecture/context-map.md
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize().toAbsolutePath();
            System.out.println("[FileController] Buscando: " + filePath);
            System.out.println("[FileController] Existe: " + filePath.toFile().exists());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                System.out.println("[FileController] Arquivo não encontrado ou não legível");
                return ResponseEntity.notFound().build();
            }

            System.out.println("[FileController] Servindo: " + filename);

            String contentType = "application/octet-stream";
            if (filename.endsWith(".png"))
                contentType = "image/png";
            else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
                contentType = "image/jpeg";
            else if (filename.endsWith(".pdf"))
                contentType = "application/pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            System.err.println("[FileController] Erro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
