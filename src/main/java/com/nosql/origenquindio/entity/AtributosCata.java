package com.nosql.origenquindio.entity;

import jakarta.validation.constraints.*;

import java.util.stream.DoubleStream;

/** Diez atributos del formato de catación, cada uno de 0 a 10. */
public record AtributosCata(
        @NotNull @DecimalMin("0") @DecimalMax("10") Double aroma,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double sabor,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double saborResidual,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double acidez,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double cuerpo,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double balance,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double uniformidad,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double tazaLimpia,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double dulzor,
        @NotNull @DecimalMin("0") @DecimalMax("10") Double puntajeCatador) {

    public double suma() {
        return DoubleStream.of(aroma, sabor, saborResidual, acidez, cuerpo, balance, uniformidad, tazaLimpia,
                dulzor, puntajeCatador).sum();
    }
}
