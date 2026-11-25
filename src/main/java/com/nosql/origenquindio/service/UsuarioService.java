package com.nosql.origenquindio.service;

import com.nosql.origenquindio.config.Colecciones;
import com.nosql.origenquindio.dto.UsuarioRequest;
import com.nosql.origenquindio.dto.UsuarioUpdateRequest;
import com.nosql.origenquindio.entity.Rol;
import com.nosql.origenquindio.entity.Usuario;
import com.nosql.origenquindio.exception.ConflictoException;
import com.nosql.origenquindio.exception.RecursoNoEncontradoException;
import com.nosql.origenquindio.exception.ReglaNegocioException;
import com.nosql.origenquindio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ReferenciaService referencias;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Usuario crear(UsuarioRequest request) {
        return usuarioRepository.save(Usuario.builder()
                .nombre(request.nombre())
                .email(normalizar(request.email()))
                .passwordHash(encoder.encode(request.password()))
                .telefono(request.telefono())
                .rol(request.rol())
                .creadoEn(Instant.now())
                .build());
    }

    public List<Usuario> listar(Rol rol) {
        return rol == null ? usuarioRepository.findAll() : usuarioRepository.findByRol(rol);
    }

    public Usuario buscar(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("usuario", id));
    }

    public Usuario exigirRol(String id, Rol rol) {
        Usuario usuario = buscar(id);
        if (usuario.getRol() != rol) {
            throw new ReglaNegocioException("El usuario " + id + " no tiene el rol " + rol);
        }
        return usuario;
    }

    public Usuario actualizar(String id, UsuarioUpdateRequest request) {
        Usuario usuario = buscar(id);
        usuario.setNombre(request.nombre());
        usuario.setEmail(normalizar(request.email()));
        usuario.setTelefono(request.telefono());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(String id) {
        buscar(id);
        if (referencias.hayReferencias(Colecciones.FINCAS, "productorId", id)) {
            throw new ConflictoException("El usuario tiene fincas registradas");
        }
        if (referencias.hayReferencias(Colecciones.CATAS, "catadorId", id)) {
            throw new ConflictoException("El usuario tiene catas registradas");
        }
        usuarioRepository.deleteById(id);
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
