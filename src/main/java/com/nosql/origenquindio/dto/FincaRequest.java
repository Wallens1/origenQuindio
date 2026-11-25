package com.nosql.origenquindio.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record FincaRequest(
        @NotBlank String nombre,
        @NotBlank String productorId,
        @NotBlank String municipio,
        String vereda,
        @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitud,
        @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitud,
        @NotNull @Min(0) @Max(3000) Integer altitudMsnm,
        @NotNull @Positive Double areaHectareas,
        List<@NotBlank String> variedades) {
}
