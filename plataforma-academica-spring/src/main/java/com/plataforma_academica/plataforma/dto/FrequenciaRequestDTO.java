package com.plataforma_academica.plataforma.dto;
import java.util.UUID;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FrequenciaRequestDTO {
    private UUID alunoId;
    private UUID salaId;
    private LocalDate data;
    private Boolean presente;
    private String justificativa;
}
