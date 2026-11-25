package com.nosql.origenquindio.controllers;

import com.nosql.origenquindio.dto.CataRequest;
import com.nosql.origenquindio.dto.PuntajePorVariedad;
import com.nosql.origenquindio.entity.Cata;
import com.nosql.origenquindio.service.CataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catas")
@RequiredArgsConstructor
public class CataController {

    private final CataService cataService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cata crear(@Valid @RequestBody CataRequest request) {
        return cataService.crear(request);
    }

    @GetMapping
    public List<Cata> listar(@RequestParam(required = false) String loteId) {
        return cataService.listar(loteId);
    }

    @GetMapping("/promedio-por-variedad")
    public List<PuntajePorVariedad> puntajePorVariedad() {
        return cataService.puntajePorVariedad();
    }

    @GetMapping("/{id}")
    public Cata buscar(@PathVariable String id) {
        return cataService.buscar(id);
    }

    @PutMapping("/{id}")
    public Cata actualizar(@PathVariable String id, @Valid @RequestBody CataRequest request) {
        return cataService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        cataService.eliminar(id);
    }
}
