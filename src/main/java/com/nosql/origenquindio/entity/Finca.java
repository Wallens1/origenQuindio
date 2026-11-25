package com.nosql.origenquindio.entity;

import com.nosql.origenquindio.config.Colecciones;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Colecciones.FINCAS)
public class Finca {

    @Id
    private String id;
    private String nombre;
    @Field(targetType = FieldType.OBJECT_ID)
    private String productorId;
    private String municipio;
    private String vereda;
    private GeoJsonPoint ubicacion;
    private Integer altitudMsnm;
    private Double areaHectareas;
    private List<String> variedades;
    private Instant creadoEn;
}
