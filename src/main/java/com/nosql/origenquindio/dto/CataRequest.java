package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.AtributosCata;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record CataRequest(
        @NotBlank String loteId,
        @NotBlank String catadorId,
        @NotNull @PastOrPresent LocalDate fecha,
        @NotNull @Valid AtributosCata atributos,
        @NotNull @PositiveOrZero Double defectos,
        List<@NotBlank String> notas) {
}
