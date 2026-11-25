package com.nosql.origenquindio.dto;

import com.nosql.origenquindio.entity.Finca;

import java.time.Instant;
import java.util.List;

public record FincaResponse(String id, String nombre, String productorId, String municipio, String vereda,
        double longitud, double latitud, Integer altitudMsnm, Double areaHectareas, List<String> variedades,
        Instant creadoEn) {

    public static FincaResponse de(Finca f) {
        return new FincaResponse(f.getId(), f.getNombre(), f.getProductorId(), f.getMunicipio(), f.getVereda(),
                f.getUbicacion().getX(), f.getUbicacion().getY(), f.getAltitudMsnm(), f.getAreaHectareas(),
                f.getVariedades(), f.getCreadoEn());
    }
}
