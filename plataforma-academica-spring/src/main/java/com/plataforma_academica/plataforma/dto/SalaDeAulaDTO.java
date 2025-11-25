package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.util.List;

@Data
public class SalaDeAulaDTO {
    private Long id;
    private String nome;
    private Long criadorId;               // evita enviar Usuario inteiro
    private List<Long> membrosIds;        // lista simples
    private List<Long> atividadesIds;     // leve
}
