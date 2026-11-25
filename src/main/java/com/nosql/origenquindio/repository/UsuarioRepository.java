package com.nosql.origenquindio.repository;

import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    List<Usuario> findByRol(Rol rol);
}
