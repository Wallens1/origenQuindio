package com.nosql.origenquindio.controllers;

import com.nosql.origenquindio.dto.EtapaRequest;
import com.nosql.origenquindio.dto.LoteRequest;
import com.nosql.origenquindio.dto.LoteUpdateRequest;
import com.nosql.origenquindio.entity.Lote;
import com.nosql.origenquindio.entity.Proceso;
import com.nosql.origenquindio.service.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lote crear(@Valid @RequestBody LoteRequest request) {
        return loteService.crear(request);
    }

    @GetMapping
    public List<Lote> listar(@RequestParam(required = false) String fincaId,
            @RequestParam(required = false) String variedad, @RequestParam(required = false) Proceso proceso,
            @RequestParam(required = false) Integer altitudMin) {
        return loteService.listar(fincaId, variedad, proceso, altitudMin);
    }

    @GetMapping("/{id}")
    public Lote buscar(@PathVariable String id) {
        return loteService.buscar(id);
    }

    @GetMapping("/codigo/{codigo}")
    public Lote buscarPorCodigo(@PathVariable String codigo) {
        return loteService.buscarPorCodigo(codigo);
    }

    @PutMapping("/{id}")
    public Lote actualizar(@PathVariable String id, @Valid @RequestBody LoteUpdateRequest request) {
        return loteService.actualizar(id, request);
    }

    @PostMapping("/{id}/etapas")
    @ResponseStatus(HttpStatus.CREATED)
    public Lote agregarEtapa(@PathVariable String id, @Valid @RequestBody EtapaRequest request) {
        return loteService.agregarEtapa(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String id) {
        loteService.eliminar(id);
    }
}
