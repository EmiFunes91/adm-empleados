package com.api.adm.service.impl;

import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import com.api.adm.entity.Producto;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CompraRepository;
import com.api.adm.service.CompraService;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoService productoService;

    @Autowired
    public CompraServiceImpl(CompraRepository compraRepository, ProductoService productoService) {
        this.compraRepository = compraRepository;
        this.productoService = productoService;
    }

    @Override
    public Compra guardarCompra(Compra compra, Long clienteId, List<CompraDetalle> detalles) {
        detalles.forEach(detalle -> {
            productoService.reducirStock(detalle.getProducto().getId(), detalle.getCantidad());
            detalle.calcularSubtotal();
        });
        compra.calcularTotal();
        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAllWithDetalles();
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada"));
    }

    @Override
    public List<Compra> buscarPorClienteOFecha(String query) {
        return compraRepository.findByClienteOrFechaContainingIgnoreCase(query);
    }

    @Override
    public Compra actualizarCompra(Long id, Compra compraActualizada) {
        Compra compraExistente = obtenerCompraPorId(id);
        revertirStock(compraExistente);  // Revertir stock antes de la actualización

        compraActualizada.getDetalles().forEach(detalle -> {
            productoService.reducirStock(detalle.getProducto().getId(), detalle.getCantidad());
            detalle.calcularSubtotal();
        });
        compraActualizada.calcularTotal();
        return compraRepository.save(compraActualizada);
    }

    @Override
    public void eliminarCompra(Long id) {
        Compra compra = obtenerCompraPorId(id);
        revertirStock(compra);  // Revertir stock al eliminar la compra
        compraRepository.deleteById(id);
    }

    @Override
    public void revertirStock(Compra compra) {
        compra.getDetalles().forEach(detalle ->
                productoService.aumentarStock(detalle.getProducto().getId(), detalle.getCantidad())
        );
    }

}




