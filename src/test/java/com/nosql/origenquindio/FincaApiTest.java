package com.nosql.origenquindio;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FincaApiTest extends PruebaApi {

    private String cuerpo(String productorId, String nombre) {
        return """
                {"nombre":"%s","productorId":"%s","municipio":"Salento","vereda":"Boquía",
                 "longitud":-75.57,"latitud":4.64,"altitudMsnm":1850,"areaHectareas":4.5,"variedades":["Castillo"]}
                """.formatted(nombre, productorId);
    }

    private String clima(String fecha, double min, double max) {
        return """
                {"fecha":"%s","temperaturaMinC":%s,"temperaturaMaxC":%s,"precipitacionMm":6.2,
                 "humedadRelativa":84,"fuente":"Estación de la finca"}
                """.formatted(fecha, min, max);
    }

    @Test
    void crudDeFincaYBusquedaPorCercania() throws Exception {
        String productor = crearUsuario("p@correo.co", "PRODUCTOR");
        String id = crear("/api/fincas", cuerpo(productor, "La Esperanza")).get("id").asString();

        Document guardada = mongo.getCollection("fincas").find().first();
        assertInstanceOf(ObjectId.class, guardada.get("productorId"));
        llamar(HttpMethod.GET, "/api/fincas?municipio=salento", null)
                .andExpect(jsonPath("$.length()").value(1));
        llamar(HttpMethod.GET, "/api/fincas/cercanas?longitud=-75.57&latitud=4.65&km=5", null)
                .andExpect(jsonPath("$.length()").value(1));
        llamar(HttpMethod.GET, "/api/fincas/cercanas?longitud=-76.53&latitud=3.42&km=5", null)
                .andExpect(jsonPath("$.length()").value(0));
        llamar(HttpMethod.PUT, "/api/fincas/" + id, cuerpo(productor, "El Mirador"))
                .andExpect(jsonPath("$.nombre").value("El Mirador"));
        llamar(HttpMethod.DELETE, "/api/usuarios/" + productor, null).andExpect(status().isConflict());
        llamar(HttpMethod.DELETE, "/api/fincas/" + id, null).andExpect(status().isNoContent());
    }

    @Test
    void soloUnProductorPuedeTenerFincas() throws Exception {
        String catador = crearUsuario("c@correo.co", "CATADOR");
        llamar(HttpMethod.POST, "/api/fincas", cuerpo(catador, "X")).andExpect(status().isBadRequest());
        llamar(HttpMethod.POST, "/api/fincas", cuerpo("no-es-id", "X")).andExpect(status().isBadRequest());
    }

    @Test
    void registraClimaPorDia() throws Exception {
        String productor = crearUsuario("p@correo.co", "PRODUCTOR");
        String finca = crear("/api/fincas", cuerpo(productor, "La Esperanza")).get("id").asString();
        String ruta = "/api/fincas/" + finca + "/clima";
        crear(ruta, clima("2026-10-01", 14, 24));
        String id = crear(ruta, clima("2026-10-02", 15, 25)).get("id").asString();

        Document guardado = mongo.getCollection("registros_clima").find(new Document("_id", new ObjectId(id))).first();
        assertEquals(Instant.parse("2026-10-02T00:00:00Z"), guardado.getDate("fecha").toInstant());
        llamar(HttpMethod.POST, ruta, clima("2026-10-01", 13, 20)).andExpect(status().isConflict());
        llamar(HttpMethod.POST, ruta, clima("2026-10-03", 30, 20)).andExpect(status().isBadRequest());
        llamar(HttpMethod.GET, ruta + "?desde=2026-10-02&hasta=2026-10-02", null)
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fecha").value("2026-10-02"));
        llamar(HttpMethod.DELETE, "/api/fincas/" + finca, null).andExpect(status().isConflict());
        llamar(HttpMethod.PUT, ruta + "/" + id, clima("2026-10-02", 12, 22))
                .andExpect(jsonPath("$.temperaturaMinC").value(12));
        llamar(HttpMethod.DELETE, ruta + "/" + id, null).andExpect(status().isNoContent());
    }
}
