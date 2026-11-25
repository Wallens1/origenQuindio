package com.nosql.origenquindio.config;

import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoIndexConfig {

    @Bean
    public CommandLineRunner crearIndices(MongoTemplate mongo) {
        return args -> {
            mongo.indexOps(Colecciones.USUARIOS).createIndex(new Index().on("email", Direction.ASC).unique());

            mongo.indexOps(Colecciones.FINCAS).createIndex(new Index().on("productorId", Direction.ASC));
            mongo.indexOps(Colecciones.FINCAS).createIndex(new Index().on("municipio", Direction.ASC));
            mongo.indexOps(Colecciones.FINCAS).createIndex(
                    new GeospatialIndex("ubicacion").typed(GeoSpatialIndexType.GEO_2DSPHERE));

            mongo.indexOps(Colecciones.LOTES).createIndex(new Index().on("codigo", Direction.ASC).unique());
            mongo.indexOps(Colecciones.LOTES).createIndex(new CompoundIndexDefinition(
                    new Document("finca.fincaId", 1).append("fechaCosecha", -1)));
            mongo.indexOps(Colecciones.LOTES).createIndex(new CompoundIndexDefinition(
                    new Document("variedad", 1).append("proceso", 1)));

            mongo.indexOps(Colecciones.CATAS).createIndex(new CompoundIndexDefinition(
                    new Document("loteId", 1).append("fecha", -1)));
            mongo.indexOps(Colecciones.CATAS).createIndex(new Index().on("catadorId", Direction.ASC));

            mongo.indexOps(Colecciones.REGISTROS_CLIMA).createIndex(
                    new Index().on("fincaId", Direction.ASC).on("fecha", Direction.DESC).unique());
        };
    }
}
