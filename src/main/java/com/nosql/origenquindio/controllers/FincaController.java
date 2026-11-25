package com.nosql.origenquindio.controllers;

import com.nosql.origenquindio.dto.FincaRequest;
import com.nosql.origenquindio.dto.FincaResponse;
import com.nosql.origenquindio.exception.ReglaNegocioException;
import com.nosql.origenquindio.service.FincaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fincas")
@RequiredArgsConstructor
public class FincaController {

    private final FincaService fincaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FincaResponse crear(@Valid @RequestBody FincaRequest request) {
        return FincaResponse.de(fincaService.crear(request));
    }

    @GetMapping
    public List<FincaResponse> listar(@RequestParam(required = false) String municipio) {
        return fincaService.listar(municipio).stream().map(FincaResponse::de).toList();
    }

    @GetMapping("/cercanas")
    public List<FincaResponse> cercanas(@RequestParam double longitud, @RequestParam double latitud,
            @RequestParam(defaultValue = "10") double km) {
        if (Math.abs(longitud) > 180 || Math.abs(latitud) > 90 || !(km > 0)) {
            throw new ReglaNegocioException("Coordenadas o distancia fuera de rango");
        }
        return fincaService.cercanas(longitud, latitud, km).stream().map(FincaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public FincaResponse buscar(@PathVariable String id) {
        return FincaResponse.de(fincaService.buscar(id));
    }

    @PutMapping("/{id}")
    public FincaResponse actualizar(@PathVariable String id, @Valid @RequestBody FincaRequest request) {
        return FincaResponse.de(fincaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        fincaService.eliminar(id);
    }
}
