package com.empresa.epi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AjusteRequest(@NotNull Long idEpi, @Min(0) int novoSaldo, @NotBlank String justificativa) {}
