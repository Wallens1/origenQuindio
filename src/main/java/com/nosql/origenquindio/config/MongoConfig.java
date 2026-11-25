package com.nosql.origenquindio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

    // Guarda LocalDate a medianoche UTC. Sin esto la fecha depende de la zona horaria del servidor.
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return MongoCustomConversions.create(
                MongoCustomConversions.MongoConverterConfigurationAdapter::useNativeDriverJavaTimeCodecs);
    }
}
