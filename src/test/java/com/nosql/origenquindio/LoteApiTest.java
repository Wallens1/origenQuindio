package com.nosql.origenquindio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoteApiTest extends PruebaApi {

    private String productor;
    private String finca;

    @BeforeEach
    void crearFinca() throws Exception {
        productor = crearUsuario("p@correo.co", "PRODUCTOR");
        finca = crear("/api/fincas", cuerpoFinca("La Esperanza")).get("id").asString();
    }

    private String cuerpoFinca(String nombre) {
        return """
                {"nombre":"%s","productorId":"%s","municipio":"Salento","longitud":-75.57,"latitud":4.64,
                 "altitudMsnm":1850,"areaHectareas":4.5}
                """.formatted(nombre, productor);
    }

    private String crearLote(String codigo) throws Exception {
        return crear("/api/lotes", """
                {"codigo":"%s","fincaId":"%s","variedad":"Castillo","proceso":"LAVADO",
                 "fechaCosecha":"2026-10-05","pesoKg":320,"humedadGrano":11.2}
                """.formatted(codigo, finca)).get("id").asString();
    }

    private String etapa(String tipo, String fecha) {
        return "{\"tipo\":\"%s\",\"fecha\":\"%s\",\"responsable\":\"Ana\"}".formatted(tipo, fecha);
    }

    @Test
    void crudYFiltros() throws Exception {
        String id = crearLote("oq-001");
        llamar(HttpMethod.GET, "/api/lotes/codigo/OQ-001", null)
                .andExpect(jsonPath("$.finca.nombre").value("La Esperanza"))
                .andExpect(jsonPath("$.fechaCosecha").value("2026-10-05"));
        llamar(HttpMethod.GET, "/api/lotes?proceso=LAVADO&altitudMin=1800&fincaId=" + finca, null)
                .andExpect(jsonPath("$.length()").value(1));
        llamar(HttpMethod.GET, "/api/lotes?altitudMin=1900", null).andExpect(jsonPath("$.length()").value(0));
        llamar(HttpMethod.GET, "/api/lotes?proceso=OTRO", null).andExpect(status().isBadRequest());
        llamar(HttpMethod.PUT, "/api/lotes/" + id, "{\"variedad\":\"Caturra\",\"proceso\":\"HONEY\",\"pesoKg\":300}")
                .andExpect(jsonPath("$.variedad").value("Caturra"));
        llamar(HttpMethod.POST, "/api/lotes", """
                {"codigo":"OQ-001","fincaId":"%s","variedad":"X","proceso":"LAVADO",
                 "fechaCosecha":"2026-10-05","pesoKg":1}""".formatted(finca)).andExpect(status().isConflict());
        llamar(HttpMethod.DELETE, "/api/fincas/" + finca, null).andExpect(status().isConflict());
        llamar(HttpMethod.DELETE, "/api/lotes/" + id, null).andExpect(status().isNoContent());
    }

    @Test
    void lasEtapasVanEnOrden() throws Exception {
        String ruta = "/api/lotes/" + crearLote("OQ-002") + "/etapas";
        crear(ruta, etapa("COSECHA", "2026-10-05"));
        crear(ruta, etapa("SECADO", "2026-10-09"));
        llamar(HttpMethod.POST, ruta, etapa("BENEFICIO", "2026-10-10")).andExpect(status().isBadRequest());
        llamar(HttpMethod.POST, ruta, etapa("SECADO", "2026-10-10")).andExpect(status().isBadRequest());
        llamar(HttpMethod.POST, ruta, etapa("TRILLA", "2026-10-06")).andExpect(status().isBadRequest());
        llamar(HttpMethod.GET, "/api/lotes/codigo/OQ-002", null)
                .andExpect(jsonPath("$.etapas.length()").value(2));
    }

    @Test
    void alEditarLaFincaCambiaLaCopiaDelLote() throws Exception {
        crearLote("OQ-003");
        llamar(HttpMethod.PUT, "/api/fincas/" + finca, cuerpoFinca("El Mirador")).andExpect(status().isOk());
        llamar(HttpMethod.GET, "/api/lotes/codigo/OQ-003", null)
                .andExpect(jsonPath("$.finca.nombre").value("El Mirador"));
    }
}
