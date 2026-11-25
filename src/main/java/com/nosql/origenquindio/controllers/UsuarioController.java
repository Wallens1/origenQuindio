package com.nosql.origenquindio.controllers;

import com.nosql.origenquindio.dto.UsuarioRequest;
import com.nosql.origenquindio.dto.UsuarioResponse;
import com.nosql.origenquindio.dto.UsuarioUpdateRequest;
import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        return UsuarioResponse.de(usuarioService.crear(request));
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) Rol rol) {
        return usuarioService.listar(rol).stream().map(UsuarioResponse::de).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable String id) {
        return UsuarioResponse.de(usuarioService.buscar(id));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable String id, @Valid @RequestBody UsuarioUpdateRequest request) {
        return UsuarioResponse.de(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
    }
}
