package com.api.adm.service.impl;

import com.api.adm.entity.CompraDetalle;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CompraDetalleRepository;
import com.api.adm.service.CompraDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompraDetalleServiceImpl implements CompraDetalleService {

    @Autowired
    private CompraDetalleRepository compraDetalleRepository;

    @Override
    public List<CompraDetalle> obtenerDetallesPorCompra(Long compraId) {
        return compraDetalleRepository.findByCompraId(compraId);
    }

    @Override
    public CompraDetalle agregarDetalle(CompraDetalle compraDetalle) {
        compraDetalle.calcularSubtotal(); // Asegura que el subtotal esté actualizado
        return compraDetalleRepository.save(compraDetalle);
    }

    @Override
    public CompraDetalle crearOActualizarDetalle(CompraDetalle compraDetalle) {
        compraDetalle.calcularSubtotal();
        return compraDetalleRepository.save(compraDetalle);
    }

    @Override
    public void eliminarDetalle(Long detalleId) {
        CompraDetalle detalle = compraDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de compra con ID " + detalleId + " no encontrado"));
        compraDetalleRepository.delete(detalle);
    }

    @Override
    public CompraDetalle actualizarDetalle(Long detalleId, CompraDetalle compraDetalleActualizado) {
        CompraDetalle detalleExistente = compraDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de compra con ID " + detalleId + " no encontrado"));

        detalleExistente.setCantidad(compraDetalleActualizado.getCantidad());
        detalleExistente.setPrecioUnitario(compraDetalleActualizado.getPrecioUnitario());
        detalleExistente.setProducto(compraDetalleActualizado.getProducto());
        detalleExistente.calcularSubtotal();

        return compraDetalleRepository.save(detalleExistente);
    }
}



