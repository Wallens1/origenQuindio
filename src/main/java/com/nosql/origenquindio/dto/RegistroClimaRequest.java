package com.nosql.origenquindio.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegistroClimaRequest(
        @NotNull @PastOrPresent LocalDate fecha,
        @NotNull Double temperaturaMinC,
        @NotNull Double temperaturaMaxC,
        @NotNull @PositiveOrZero Double precipitacionMm,
        @DecimalMin("0") @DecimalMax("100") Double humedadRelativa,
        @NotBlank String fuente) {
}
