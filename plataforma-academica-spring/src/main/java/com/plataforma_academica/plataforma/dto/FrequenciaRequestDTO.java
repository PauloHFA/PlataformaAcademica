package com.plataforma_academica.plataforma.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FrequenciaRequestDTO {
    private Long alunoId;
    private Long salaId;
    private LocalDate data;
    private Boolean presente;
    private String justificativa;
}
