package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.util.List;

@Data
public class SalaDeAulaResponseDTO {

    private Long id;
    private String nome;

    private Long criadorId;
    private String criadorNome;       // nome do criador (útil para exibição)

    private List<Long> membrosIds;
    private List<String> membrosNomes;   // nomes dos membros – opcional mas muito útil

    private List<Long> atividadesIds;
}
