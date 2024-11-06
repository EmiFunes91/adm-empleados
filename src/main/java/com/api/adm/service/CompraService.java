package com.api.adm.service;

import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import java.util.List;

public interface CompraService {
    Compra guardarCompra(Compra compra, Long clienteId, List<CompraDetalle> detalles);
    List<Compra> obtenerTodasLasCompras();
    Compra obtenerCompraPorId(Long id);
    List<Compra> buscarPorClienteOFecha(String query);
    Compra actualizarCompra(Long id, Compra compraActualizada);
    void eliminarCompra(Long id);
}


