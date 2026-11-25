package com.nosql.origenquindio.entity;

import com.nosql.origenquindio.config.Colecciones;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = Colecciones.USUARIOS)
public class Usuario {

    @Id
    private String id;
    private String nombre;
    private String email;
    private String passwordHash;
    private String telefono;
    private Rol rol;
    private Instant creadoEn;
}
