package com.nosql.origenquindio.repository;

import com.nosql.origenquindio.entity.RegistroClima;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RegistroClimaRepository extends MongoRepository<RegistroClima, String> {

    List<RegistroClima> findByFincaIdOrderByFechaDesc(String fincaId);

    @Query(value = "{ 'fincaId': ?0, 'fecha': { $gte: ?1, $lte: ?2 } }", sort = "{ 'fecha': -1 }")
    List<RegistroClima> buscarEnRango(org.bson.types.ObjectId fincaId, LocalDate desde, LocalDate hasta);
}
