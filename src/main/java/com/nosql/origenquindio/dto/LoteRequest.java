package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.Proceso;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record LoteRequest(
        @NotBlank String codigo,
        @NotBlank String fincaId,
        @NotBlank String variedad,
        @NotNull Proceso proceso,
        @NotNull @PastOrPresent LocalDate fechaCosecha,
        @NotNull @Positive Double pesoKg,
        @DecimalMin("0") @DecimalMax("100") Double humedadGrano) {
}
