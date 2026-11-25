package com.nosql.origenquindio.service;

import com.nosql.origenquindio.config.Colecciones;
import com.nosql.origenquindio.dto.CataRequest;
import com.nosql.origenquindio.dto.PuntajePorVariedad;
import com.nosql.origenquindio.entity.Cata;
import com.nosql.origenquindio.entity.Lote;
import com.nosql.origenquindio.entity.ResumenCatas;
import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.exception.RecursoNoEncontradoException;
import com.nosql.origenquindio.exception.ReglaNegocioException;
import com.nosql.origenquindio.repository.CataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class CataService {

    private final CataRepository cataRepository;
    private final LoteService loteService;
    private final UsuarioService usuarioService;
    private final ReferenciaService referencias;
    private final MongoTemplate mongo;

    public Cata crear(CataRequest request) {
        Cata cata = cataRepository.save(aplicar(new Cata(), request));
        actualizarResumen(cata.getLoteId());
        return cata;
    }

    public List<Cata> listar(String loteId) {
        if (loteId == null) {
            return cataRepository.findAll();
        }
        referencias.exigirIdValido("loteId", loteId);
        return cataRepository.findByLoteIdOrderByFechaDesc(loteId);
    }

    public Cata buscar(String id) {
        return cataRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("cata", id));
    }

    public Cata actualizar(String id, CataRequest request) {
        Cata cata = buscar(id);
        String loteAnterior = cata.getLoteId();
        cata = cataRepository.save(aplicar(cata, request));
        actualizarResumen(cata.getLoteId());
        if (!loteAnterior.equals(cata.getLoteId())) {
            actualizarResumen(loteAnterior);
        }
        return cata;
    }

    public void eliminar(String id) {
        Cata cata = buscar(id);
        cataRepository.delete(cata);
        actualizarResumen(cata.getLoteId());
    }

    public List<PuntajePorVariedad> puntajePorVariedad() {
        Aggregation pipeline = newAggregation(
                lookup(Colecciones.LOTES, "loteId", "_id", "lote"),
                unwind("lote"),
                group("lote.variedad").addToSet("loteId").as("lotes").count().as("catas")
                        .avg("puntajeTotal").as("puntajePromedio"),
                project("catas").and("_id").as("variedad")
                        .and("lotes").size().as("lotes")
                        .andExpression("round(puntajePromedio, 2)").as("puntajePromedio")
                        .andExclude("_id"),
                sort(Sort.Direction.DESC, "puntajePromedio"));
        return mongo.aggregate(pipeline, Colecciones.CATAS, PuntajePorVariedad.class).getMappedResults();
    }

    private Cata aplicar(Cata cata, CataRequest request) {
        referencias.exigirIdValido("loteId", request.loteId());
        referencias.exigirIdValido("catadorId", request.catadorId());
        Lote lote = loteService.buscar(request.loteId());
        usuarioService.exigirRol(request.catadorId(), Rol.CATADOR);
        if (request.fecha().isBefore(lote.getFechaCosecha())) {
            throw new ReglaNegocioException("La cata es anterior a la cosecha del lote");
        }
        double total = request.atributos().suma() - request.defectos();
        if (total < 0) {
            throw new ReglaNegocioException("Los defectos superan la suma de los atributos");
        }
        cata.setLoteId(request.loteId());
        cata.setCatadorId(request.catadorId());
        cata.setFecha(request.fecha());
        cata.setAtributos(request.atributos());
        cata.setDefectos(request.defectos());
        cata.setPuntajeTotal(redondear(total));
        cata.setNotas(request.notas() == null ? List.of() : request.notas());
        return cata;
    }

    private void actualizarResumen(String loteId) {
        List<Cata> catas = cataRepository.findByLoteIdOrderByFechaDesc(loteId);
        Double promedio = catas.isEmpty() ? null
                : redondear(catas.stream().mapToDouble(Cata::getPuntajeTotal).average().getAsDouble());
        mongo.updateFirst(Query.query(Criteria.where("id").is(loteId)),
                new Update().set("resumenCatas", new ResumenCatas(catas.size(), promedio)), Lote.class);
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}
