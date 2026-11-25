package com.nosql.origenquindio.entity;

import com.nosql.origenquindio.config.Colecciones;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Colecciones.LOTES)
public class Lote {

    @Id
    private String id;
    private String codigo;
    private FincaResumen finca;
    private String variedad;
    private Proceso proceso;
    private LocalDate fechaCosecha;
    private Double pesoKg;
    private Double humedadGrano;
    private List<Etapa> etapas;
    private ResumenCatas resumenCatas;
    private Instant creadoEn;
}
