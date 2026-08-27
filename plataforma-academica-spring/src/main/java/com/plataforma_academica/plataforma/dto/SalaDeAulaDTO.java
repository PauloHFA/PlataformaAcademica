package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO de transferência para Salas de Aula.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SaladeAula
 * @see SalaDeAulaMapper
 */
@Data
public class SalaDeAulaDTO {
    private Long id;
    private String nome;
    private Long criadorId; // evita enviar Usuario inteiro
    private List<Long> membrosIds; // lista simples
    private List<Long> atividadesIds; // leve
}
