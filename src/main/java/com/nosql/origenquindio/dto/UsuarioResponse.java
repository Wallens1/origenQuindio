package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.entity.Usuario;

import java.time.Instant;

/** No incluye passwordHash para que nunca salga en una respuesta. */
public record UsuarioResponse(String id, String nombre, String email, String telefono, Rol rol, Instant creadoEn) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getTelefono(), u.getRol(),
                u.getCreadoEn());
    }
}
