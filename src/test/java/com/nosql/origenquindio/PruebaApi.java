package com.nosql.origenquindio;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

/** Base de las pruebas. Corre contra MongoDB, en la base origen_quindio_test. */
@SpringBootTest
@AutoConfigureMockMvc
abstract class PruebaApi {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected MongoTemplate mongo;
    private final JsonMapper json = new JsonMapper();

    @BeforeEach
    void limpiar() {
        assertEquals("origen_quindio_test", mongo.getDb().getName());
        mongo.getCollectionNames().forEach(c -> mongo.remove(new Query(), c));
    }

    protected ResultActions llamar(HttpMethod metodo, String ruta, String cuerpo) throws Exception {
        var peticion = request(metodo, ruta);
        if (cuerpo != null) {
            peticion.contentType(MediaType.APPLICATION_JSON).content(cuerpo);
        }
        return mvc.perform(peticion);
    }

    protected JsonNode crear(String ruta, String cuerpo) throws Exception {
        var respuesta = llamar(HttpMethod.POST, ruta, cuerpo).andReturn().getResponse();
        assertEquals(201, respuesta.getStatus(), respuesta.getContentAsString());
        return json.readTree(respuesta.getContentAsByteArray());
    }

    protected String crearUsuario(String email, String rol) throws Exception {
        return crear("/api/usuarios", """
                {"nombre":"Persona de prueba","email":"%s","password":"clave-de-prueba","rol":"%s"}
                """.formatted(email, rol)).get("id").asString();
    }
}
