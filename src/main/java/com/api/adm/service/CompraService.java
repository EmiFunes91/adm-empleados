package com.api.adm.service;

import com.api.adm.entity.Compra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CompraService {
    Compra crearCompra(Compra compra);
    Compra obtenerCompraPorId(Long id);

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.compraDetalles WHERE c.id = :id")
    Compra obtenerCompraPorIdConDetalles(@Param("id") Long id);

    List<Compra> obtenerComprasPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Compra> obtenerComprasPorProveedor(Long clienteId);
    List<Compra> obtenerComprasPorEstado(String estado);
    Compra actualizarCompra(Long id, Compra compraActualizada);
    void eliminarCompra(Long id);
    List<Compra> obtenerComprasFiltradas(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long clienteId, String estado);

    // Nuevo método para obtener compras con detalles en un rango de fechas
    List<Compra> obtenerComprasConDetallesPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}








