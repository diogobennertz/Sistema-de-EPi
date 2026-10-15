package com.empresa.epi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record DevolucaoRequest(
        @Min(1) int quantidade,
        LocalDate data,
        @NotBlank String condicao,
        String observacao) {}
