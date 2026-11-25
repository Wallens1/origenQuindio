package com.nosql.origenquindio.service;

import com.nosql.origenquindio.config.Colecciones;
import com.nosql.origenquindio.dto.FincaRequest;
import com.nosql.origenquindio.entity.Finca;
import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.exception.ConflictoException;
import com.nosql.origenquindio.exception.RecursoNoEncontradoException;
import com.nosql.origenquindio.repository.FincaRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FincaService {

    private final FincaRepository fincaRepository;
    private final UsuarioService usuarioService;
    private final ReferenciaService referencias;
    private final MongoTemplate mongo;

    public Finca crear(FincaRequest request) {
        Finca finca = Finca.builder().creadoEn(Instant.now()).build();
        return fincaRepository.save(aplicar(finca, request));
    }

    public List<Finca> listar(String municipio) {
        return municipio == null ? fincaRepository.findAll() : fincaRepository.findByMunicipioIgnoreCase(municipio);
    }

    public List<Finca> cercanas(double longitud, double latitud, double km) {
        return fincaRepository.findByUbicacionNear(new Point(longitud, latitud),
                new Distance(km, Metrics.KILOMETERS));
    }

    public Finca buscar(String id) {
        return fincaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("finca", id));
    }

    public Finca actualizar(String id, FincaRequest request) {
        Finca finca = fincaRepository.save(aplicar(buscar(id), request));
        // Los lotes guardan una copia de estos tres campos.
        mongo.updateMulti(Query.query(Criteria.where("finca.fincaId").is(new ObjectId(id))),
                new Update().set("finca.nombre", finca.getNombre())
                        .set("finca.municipio", finca.getMunicipio())
                        .set("finca.altitudMsnm", finca.getAltitudMsnm()),
                Colecciones.LOTES);
        return finca;
    }

    public void eliminar(String id) {
        buscar(id);
        if (referencias.hayReferencias(Colecciones.LOTES, "finca.fincaId", id)) {
            throw new ConflictoException("La finca tiene lotes registrados");
        }
        if (referencias.hayReferencias(Colecciones.REGISTROS_CLIMA, "fincaId", id)) {
            throw new ConflictoException("La finca tiene registros de clima");
        }
        fincaRepository.deleteById(id);
    }

    private Finca aplicar(Finca finca, FincaRequest request) {
        referencias.exigirIdValido("productorId", request.productorId());
        usuarioService.exigirRol(request.productorId(), Rol.PRODUCTOR);
        finca.setNombre(request.nombre());
        finca.setProductorId(request.productorId());
        finca.setMunicipio(request.municipio());
        finca.setVereda(request.vereda());
        finca.setUbicacion(new GeoJsonPoint(request.longitud(), request.latitud()));
        finca.setAltitudMsnm(request.altitudMsnm());
        finca.setAreaHectareas(request.areaHectareas());
        finca.setVariedades(request.variedades() == null ? List.of() : request.variedades());
        return finca;
    }
}
