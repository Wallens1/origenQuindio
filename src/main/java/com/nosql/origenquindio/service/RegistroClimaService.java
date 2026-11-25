package com.nosql.origenquindio.service;

import com.nosql.origenquindio.dto.RegistroClimaRequest;
import com.nosql.origenquindio.entity.RegistroClima;
import com.nosql.origenquindio.exception.RecursoNoEncontradoException;
import com.nosql.origenquindio.exception.ReglaNegocioException;
import com.nosql.origenquindio.repository.RegistroClimaRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroClimaService {

    private final RegistroClimaRepository climaRepository;
    private final FincaService fincaService;

    public RegistroClima crear(String fincaId, RegistroClimaRequest request) {
        fincaService.buscar(fincaId);
        return climaRepository.save(aplicar(RegistroClima.builder().fincaId(fincaId).build(), request));
    }

    public List<RegistroClima> listar(String fincaId, LocalDate desde, LocalDate hasta) {
        fincaService.buscar(fincaId);
        if (desde == null && hasta == null) {
            return climaRepository.findByFincaIdOrderByFechaDesc(fincaId);
        }
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new ReglaNegocioException("Se necesitan desde y hasta, en ese orden");
        }
        return climaRepository.buscarEnRango(new ObjectId(fincaId), desde, hasta);
    }

    public RegistroClima actualizar(String fincaId, String id, RegistroClimaRequest request) {
        return climaRepository.save(aplicar(buscar(fincaId, id), request));
    }

    public void eliminar(String fincaId, String id) {
        climaRepository.delete(buscar(fincaId, id));
    }

    private RegistroClima buscar(String fincaId, String id) {
        return climaRepository.findById(id)
                .filter(registro -> registro.getFincaId().equals(fincaId))
                .orElseThrow(() -> new RecursoNoEncontradoException("registro de clima", id));
    }

    private RegistroClima aplicar(RegistroClima registro, RegistroClimaRequest request) {
        if (request.temperaturaMinC() > request.temperaturaMaxC()) {
            throw new ReglaNegocioException("La temperatura mínima supera la máxima");
        }
        registro.setFecha(request.fecha());
        registro.setTemperaturaMinC(request.temperaturaMinC());
        registro.setTemperaturaMaxC(request.temperaturaMaxC());
        registro.setPrecipitacionMm(request.precipitacionMm());
        registro.setHumedadRelativa(request.humedadRelativa());
        registro.setFuente(request.fuente());
        return registro;
    }
}
