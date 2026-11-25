package com.nosql.origenquindio.dto;

import jakarta.validation.constraints.*;

public record UsuarioUpdateRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        String telefono) {
}
