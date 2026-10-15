package com.empresa.epi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EntradaRequest(@NotNull Long idEpi, @Min(1) int quantidade, String observacao) {}
