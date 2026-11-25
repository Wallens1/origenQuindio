package com.nosql.origenquindio.service;

import com.nosql.origenquindio.exception.ReglaNegocioException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/** MongoDB no tiene llaves foráneas: esta clase valida las referencias entre colecciones. */
@Service
@RequiredArgsConstructor
public class ReferenciaService {

    private final MongoTemplate mongo;

    public void exigirIdValido(String campo, String id) {
        if (id == null || !ObjectId.isValid(id)) {
            throw new ReglaNegocioException(campo + " no es un ObjectId válido");
        }
    }

    public boolean hayReferencias(String coleccion, String campo, String id) {
        return mongo.exists(Query.query(Criteria.where(campo).is(new ObjectId(id))), coleccion);
    }
}
