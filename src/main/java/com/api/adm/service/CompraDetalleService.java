package com.api.adm.service;

import com.api.adm.entity.CompraDetalle;
import java.util.List;

public interface CompraDetalleService {
    List<CompraDetalle> obtenerDetallesPorCompra(Long compraId);
    CompraDetalle agregarDetalle(CompraDetalle compraDetalle);
    CompraDetalle crearOActualizarDetalle(CompraDetalle compraDetalle);
    void eliminarDetalle(Long detalleId);
    CompraDetalle actualizarDetalle(Long detalleId, CompraDetalle compraDetalle);
}

