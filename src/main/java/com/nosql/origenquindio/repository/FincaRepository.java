package com.nosql.origenquindio.repository;

import com.nosql.origenquindio.entity.Finca;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.util.List;

public interface FincaRepository extends MongoRepository<Finca, String> {

    List<Finca> findByMunicipioIgnoreCase(String municipio);

    List<Finca> findByUbicacionNear(Point punto, Distance distancia);
}
