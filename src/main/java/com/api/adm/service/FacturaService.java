package com.api.adm.service;

import com.api.adm.entity.Factura;
import com.api.adm.entity.FacturaDetalle;

import java.io.IOException;
import java.util.List;

public interface FacturaService {
    List<Factura> obtenerTodasLasFacturas();
    Factura obtenerFacturaPorId(Long id);
    Factura crearFactura(Factura factura, Long clienteId, List<FacturaDetalle> detalles);
    Factura actualizarFactura(Long id, Factura facturaActualizada);
    void eliminarFactura(Long id);
    byte[] generarFacturaPdf(Long id) throws IOException;
}






