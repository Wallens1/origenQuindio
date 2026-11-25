package com.nosql.origenquindio.repository;

import com.nosql.origenquindio.entity.Cata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CataRepository extends MongoRepository<Cata, String> {

    List<Cata> findByLoteIdOrderByFechaDesc(String loteId);
}
