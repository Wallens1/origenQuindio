package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.Rol;
import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 64) String password,
        String telefono,
        @NotNull Rol rol) {
}
