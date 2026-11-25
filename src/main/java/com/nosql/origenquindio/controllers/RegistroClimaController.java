package com.nosql.origenquindio.controllers;

import com.nosql.origenquindio.dto.RegistroClimaRequest;
import com.nosql.origenquindio.entity.RegistroClima;
import com.nosql.origenquindio.service.RegistroClimaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fincas/{fincaId}/clima")
@RequiredArgsConstructor
public class RegistroClimaController {

    private final RegistroClimaService climaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroClima crear(@PathVariable String fincaId, @Valid @RequestBody RegistroClimaRequest request) {
        return climaService.crear(fincaId, request);
    }

    @GetMapping
    public List<RegistroClima> listar(@PathVariable String fincaId,
            @RequestParam(required = false) LocalDate desde, @RequestParam(required = false) LocalDate hasta) {
        return climaService.listar(fincaId, desde, hasta);
    }

    @PutMapping("/{id}")
    public RegistroClima actualizar(@PathVariable String fincaId, @PathVariable String id,
            @Valid @RequestBody RegistroClimaRequest request) {
        return climaService.actualizar(fincaId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String fincaId, @PathVariable String id) {
        climaService.eliminar(fincaId, id);
    }
}
