package com.nosql.origenquindio.repository;

import com.nosql.origenquindio.entity.Lote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoteRepository extends MongoRepository<Lote, String> {

    Optional<Lote> findByCodigo(String codigo);
}
