package com.nosql.origenquindio.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, String id) {
        super("No existe " + recurso + " con id " + id);
    }
}
