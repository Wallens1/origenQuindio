package com.nosql.origenquindio.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

/** Copia de los datos de la finca que se muestran con cada lote. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FincaResumen {

    @Field(targetType = FieldType.OBJECT_ID)
    private String fincaId;
    private String nombre;
    private String municipio;
    private Integer altitudMsnm;
}
