package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.TipoEtapa;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EtapaRequest(
        @NotNull TipoEtapa tipo,
        @NotNull @PastOrPresent LocalDate fecha,
        @NotBlank String responsable,
        String observaciones) {
}
