package com.api.adm.service.impl;

import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CompraRepository;
import com.api.adm.service.CompraDetalleService;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final CompraDetalleService compraDetalleService;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, CompraDetalleService compraDetalleService) {
        this.compraRepository = compraRepository;
        this.compraDetalleService = compraDetalleService;
    }

    @Transactional
    public Compra crearCompra(Compra compra) {
        compra.setFechaCompra(LocalDateTime.now());
        for (CompraDetalle detalle : compra.getCompraDetalles()) {
            detalle.setCompra(compra);
            detalle.getProducto().reducirStock(detalle.getCantidad());
        }
        return compraRepository.save(compra);
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra con ID " + id + " no encontrada"));
    }

    @Override
    public Compra obtenerCompraPorIdConDetalles(Long id) {
        return compraRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra con ID " + id + " no encontrada"));
    }

    @Override
    public List<Compra> obtenerComprasPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Compra> obtenerComprasPorProveedor(Long clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Compra> obtenerComprasPorEstado(String estado) {
        return compraRepository.findByEstado(estado);
    }

    @Override
    public List<Compra> obtenerComprasFiltradas(LocalDateTime fechaInicio, LocalDateTime fechaFin, Long clienteId, String estado) {
        if (fechaInicio == null) fechaInicio = LocalDateTime.MIN;
        if (fechaFin == null) fechaFin = LocalDateTime.MAX;
        if (clienteId != null && estado != null) {
            return compraRepository.findByFechaCompraBetweenAndClienteIdAndEstado(fechaInicio, fechaFin, clienteId, estado);
        } else if (clienteId != null) {
            return compraRepository.findByFechaCompraBetweenAndClienteId(fechaInicio, fechaFin, clienteId);
        } else if (estado != null) {
            return compraRepository.findByFechaCompraBetweenAndEstado(fechaInicio, fechaFin, estado);
        } else {
            return compraRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
        }
    }

    @Override
    public List<Compra> obtenerComprasConDetallesPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return compraRepository.findByFechaCompraBetweenWithDetalles(fechaInicio, fechaFin);
    }

    @Transactional
    @Override
    public Compra actualizarCompra(Long id, Compra compraActualizada) {
        Compra compraExistente = obtenerCompraPorId(id);
        compraExistente.setCliente(compraActualizada.getCliente());
        compraExistente.setFechaCompra(compraActualizada.getFechaCompra());
        compraExistente.setEstado(compraActualizada.getEstado());
        compraExistente.getCompraDetalles().clear();
        for (CompraDetalle detalle : compraActualizada.getCompraDetalles()) {
            detalle.setCompra(compraExistente);
            compraDetalleService.crearOActualizarDetalle(detalle);
        }
        compraExistente.calcularTotal();
        return compraRepository.save(compraExistente);
    }

    @Transactional
    @Override
    public void eliminarCompra(Long id) {
        Compra compra = obtenerCompraPorId(id);
        compraRepository.delete(compra);
    }
}





