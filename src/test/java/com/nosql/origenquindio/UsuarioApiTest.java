package com.nosql.origenquindio;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioApiTest extends PruebaApi {

    @Test
    void creaConsultaActualizaYElimina() throws Exception {
        String id = crearUsuario("Productor@Correo.co", "PRODUCTOR");

        llamar(HttpMethod.GET, "/api/usuarios/" + id, null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("productor@correo.co"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
        llamar(HttpMethod.PUT, "/api/usuarios/" + id, "{\"nombre\":\"Otro\",\"email\":\"otro@correo.co\"}")
                .andExpect(jsonPath("$.nombre").value("Otro"));
        llamar(HttpMethod.DELETE, "/api/usuarios/" + id, null).andExpect(status().isNoContent());
        llamar(HttpMethod.GET, "/api/usuarios/" + id, null).andExpect(status().isNotFound());
    }

    @Test
    void guardaLaClaveCifrada() throws Exception {
        crearUsuario("a@correo.co", "ADMIN");
        Document guardado = mongo.getCollection("usuarios").find().first();
        assertTrue(guardado.getString("passwordHash").startsWith("$2"));
    }

    @Test
    void rechazaEmailRepetidoYDatosInvalidos() throws Exception {
        crearUsuario("a@correo.co", "CATADOR");
        llamar(HttpMethod.POST, "/api/usuarios", """
                {"nombre":"B","email":"A@correo.co","password":"clave-de-prueba","rol":"CATADOR"}
                """).andExpect(status().isConflict());
        llamar(HttpMethod.POST, "/api/usuarios", "{\"nombre\":\"\",\"email\":\"x\",\"password\":\"1\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.rol").exists());
    }
}
