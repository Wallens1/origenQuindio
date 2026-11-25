package com.nosql.origenquindio.service;

import com.nosql.origenquindio.config.Colecciones;
import com.nosql.origenquindio.dto.EtapaRequest;
import com.nosql.origenquindio.dto.LoteRequest;
import com.nosql.origenquindio.dto.LoteUpdateRequest;
import com.nosql.origenquindio.entity.*;
import com.nosql.origenquindio.exception.ConflictoException;
import com.nosql.origenquindio.exception.RecursoNoEncontradoException;
import com.nosql.origenquindio.exception.ReglaNegocioException;
import com.nosql.origenquindio.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final FincaService fincaService;
    private final ReferenciaService referencias;
    private final MongoTemplate mongo;

    public Lote crear(LoteRequest request) {
        referencias.exigirIdValido("fincaId", request.fincaId());
        Finca finca = fincaService.buscar(request.fincaId());
        return loteRepository.save(Lote.builder()
                .codigo(normalizar(request.codigo()))
                .finca(new FincaResumen(finca.getId(), finca.getNombre(), finca.getMunicipio(),
                        finca.getAltitudMsnm()))
                .variedad(request.variedad())
                .proceso(request.proceso())
                .fechaCosecha(request.fechaCosecha())
                .pesoKg(request.pesoKg())
                .humedadGrano(request.humedadGrano())
                .etapas(List.of())
                .resumenCatas(new ResumenCatas(0, null))
                .creadoEn(Instant.now())
                .build());
    }

    public List<Lote> listar(String fincaId, String variedad, Proceso proceso, Integer altitudMin) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "fechaCosecha"));
        if (fincaId != null) {
            referencias.exigirIdValido("fincaId", fincaId);
            query.addCriteria(Criteria.where("finca.fincaId").is(new ObjectId(fincaId)));
        }
        if (variedad != null) {
            query.addCriteria(Criteria.where("variedad").is(variedad));
        }
        if (proceso != null) {
            query.addCriteria(Criteria.where("proceso").is(proceso));
        }
        if (altitudMin != null) {
            query.addCriteria(Criteria.where("finca.altitudMsnm").gte(altitudMin));
        }
        return mongo.find(query, Lote.class);
    }

    public Lote buscar(String id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("lote", id));
    }

    public Lote buscarPorCodigo(String codigo) {
        return loteRepository.findByCodigo(normalizar(codigo))
                .orElseThrow(() -> new RecursoNoEncontradoException("lote", codigo));
    }

    public Lote actualizar(String id, LoteUpdateRequest request) {
        // Se actualizan solo estos campos para no pisar etapas ni resumenCatas.
        return modificar(id, porId(id), new Update()
                .set("variedad", request.variedad())
                .set("proceso", request.proceso())
                .set("pesoKg", request.pesoKg())
                .set("humedadGrano", request.humedadGrano()));
    }

    public Lote agregarEtapa(String id, EtapaRequest request) {
        Lote lote = buscar(id);
        if (request.fecha().isBefore(lote.getFechaCosecha())) {
            throw new ReglaNegocioException("La etapa es anterior a la cosecha del lote");
        }
        int cantidad = lote.getEtapas().size();
        if (cantidad > 0) {
            Etapa ultima = lote.getEtapas().get(cantidad - 1);
            if (request.tipo().compareTo(ultima.getTipo()) <= 0) {
                throw new ReglaNegocioException("La etapa " + request.tipo() + " no puede ir después de "
                        + ultima.getTipo());
            }
            if (request.fecha().isBefore(ultima.getFecha())) {
                throw new ReglaNegocioException("La fecha es anterior a la última etapa");
            }
        }
        Etapa etapa = new Etapa(request.tipo(), request.fecha(), request.responsable(), request.observaciones());
        // El filtro por tamaño evita que dos peticiones simultáneas agreguen la misma etapa.
        Lote actualizado = mongo.findAndModify(porId(id).addCriteria(Criteria.where("etapas").size(cantidad)),
                new Update().push("etapas", etapa), FindAndModifyOptions.options().returnNew(true), Lote.class);
        if (actualizado == null) {
            throw new ConflictoException("El lote cambió mientras se agregaba la etapa");
        }
        return actualizado;
    }

    public void eliminar(String id) {
        buscar(id);
        if (referencias.hayReferencias(Colecciones.CATAS, "loteId", id)) {
            throw new ConflictoException("El lote tiene catas registradas");
        }
        loteRepository.deleteById(id);
    }

    private Lote modificar(String id, Query query, Update update) {
        Lote lote = mongo.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Lote.class);
        if (lote == null) {
            throw new RecursoNoEncontradoException("lote", id);
        }
        return lote;
    }

    private Query porId(String id) {
        return Query.query(Criteria.where("id").is(id));
    }

    private String normalizar(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }
}
