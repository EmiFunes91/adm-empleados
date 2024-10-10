package com.api.adm.service;

import com.api.adm.entity.Cajero;
import java.util.List;

public interface CajeroService {
    Cajero guardarCajero(Cajero cajero);
    List<Cajero> obtenerTodosLosCajeros();
    List<Cajero> buscarCajeros(String query);
    Cajero obtenerCajeroPorId(Long id);
    Cajero actualizarCajero(Long id, Cajero cajero);
    void eliminarCajero(Long id);
}


