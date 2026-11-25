package com.nosql.origenquindio.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Etapa {

    private TipoEtapa tipo;
    private LocalDate fecha;
    private String responsable;
    private String observaciones;
}
