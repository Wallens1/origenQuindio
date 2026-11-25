package com.nosql.origenquindio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CataApiTest extends PruebaApi {

    private String productor;
    private String catador;
    private String lote;

    @BeforeEach
    void crearLote() throws Exception {
        productor = crearUsuario("p@correo.co", "PRODUCTOR");
        catador = crearUsuario("c@correo.co", "CATADOR");
        String finca = crear("/api/fincas", """
                {"nombre":"La Esperanza","productorId":"%s","municipio":"Salento","longitud":-75.57,
                 "latitud":4.64,"altitudMsnm":1850,"areaHectareas":4.5}
                """.formatted(productor)).get("id").asString();
        lote = crear("/api/lotes", """
                {"codigo":"OQ-001","fincaId":"%s","variedad":"Castillo","proceso":"LAVADO",
                 "fechaCosecha":"2026-10-05","pesoKg":320}
                """.formatted(finca)).get("id").asString();
    }

    private String cata(String catadorId, double aroma, double defectos) {
        return """
                {"loteId":"%s","catadorId":"%s","fecha":"2026-11-01","defectos":%s,"notas":["panela"],
                 "atributos":{"aroma":%s,"sabor":8,"saborResidual":8,"acidez":8,"cuerpo":8,"balance":8,
                 "uniformidad":10,"tazaLimpia":10,"dulzor":10,"puntajeCatador":8}}
                """.formatted(lote, catadorId, defectos, aroma);
    }

    @Test
    void calculaElPuntajeYActualizaElResumenDelLote() throws Exception {
        String id = crear("/api/catas", cata(catador, 8, 2)).get("id").asString();
        String otra = crear("/api/catas", cata(catador, 8, 0)).get("id").asString();
        llamar(HttpMethod.GET, "/api/catas/" + id, null).andExpect(jsonPath("$.puntajeTotal").value(84));
        llamar(HttpMethod.GET, "/api/lotes/" + lote, null)
                .andExpect(jsonPath("$.resumenCatas.cantidad").value(2))
                .andExpect(jsonPath("$.resumenCatas.puntajePromedio").value(85));
        llamar(HttpMethod.GET, "/api/catas/promedio-por-variedad", null)
                .andExpect(jsonPath("$[0].variedad").value("Castillo"))
                .andExpect(jsonPath("$[0].lotes").value(1))
                .andExpect(jsonPath("$[0].catas").value(2))
                .andExpect(jsonPath("$[0].puntajePromedio").value(85));
        llamar(HttpMethod.PUT, "/api/catas/" + id, cata(catador, 6, 2))
                .andExpect(jsonPath("$.puntajeTotal").value(82));
        llamar(HttpMethod.DELETE, "/api/catas/" + otra, null).andExpect(status().isNoContent());
        llamar(HttpMethod.GET, "/api/lotes/" + lote, null)
                .andExpect(jsonPath("$.resumenCatas.puntajePromedio").value(82));
        llamar(HttpMethod.GET, "/api/catas?loteId=" + lote, null).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void validaAtributosYRol() throws Exception {
        llamar(HttpMethod.POST, "/api/catas", cata(catador, 11, 0)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos['atributos.aroma']").exists());
        llamar(HttpMethod.POST, "/api/catas", cata(productor, 8, 0)).andExpect(status().isBadRequest());
    }

    @Test
    void protegeLoteYCatadorConCatas() throws Exception {
        crear("/api/catas", cata(catador, 8, 0));
        llamar(HttpMethod.DELETE, "/api/lotes/" + lote, null).andExpect(status().isConflict());
        llamar(HttpMethod.DELETE, "/api/usuarios/" + catador, null).andExpect(status().isConflict());
    }
}
