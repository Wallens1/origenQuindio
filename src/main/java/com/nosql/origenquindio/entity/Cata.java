package com.nosql.origenquindio.entity;

import com.nosql.origenquindio.config.Colecciones;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Colecciones.CATAS)
public class Cata {

    @Id
    private String id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String loteId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String catadorId;
    private LocalDate fecha;
    private AtributosCata atributos;
    private Double defectos;
    private Double puntajeTotal;
    private List<String> notas;
}
