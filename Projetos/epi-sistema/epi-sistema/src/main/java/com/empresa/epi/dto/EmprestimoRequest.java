package com.empresa.epi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record EmprestimoRequest(
        @NotNull Long idColaborador,
        String observacao,
        @NotEmpty @Valid List<ItemRequest> itens) {

    public record ItemRequest(
            @NotNull Long idEpi,
            @Min(1) int quantidade,
            LocalDate dataPrevistaDevolucao) {}
}
