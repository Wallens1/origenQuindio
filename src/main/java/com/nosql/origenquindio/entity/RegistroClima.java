package com.nosql.origenquindio.entity;

import com.nosql.origenquindio.config.Colecciones;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Colecciones.REGISTROS_CLIMA)
public class RegistroClima {

    @Id
    private String id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String fincaId;
    private LocalDate fecha;
    private Double temperaturaMinC;
    private Double temperaturaMaxC;
    private Double precipitacionMm;
    private Double humedadRelativa;
    private String fuente;
}
