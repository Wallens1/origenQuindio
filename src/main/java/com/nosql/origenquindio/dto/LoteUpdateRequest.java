package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.Proceso;
import jakarta.validation.constraints.*;

/** El código, la finca y la fecha de cosecha identifican el origen del lote y no se editan. */
public record LoteUpdateRequest(
        @NotBlank String variedad,
        @NotNull Proceso proceso,
        @NotNull @Positive Double pesoKg,
        @DecimalMin("0") @DecimalMax("100") Double humedadGrano) {
}
