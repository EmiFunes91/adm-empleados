package com.api.adm.service.impl;

import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import com.api.adm.entity.Producto;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CompraRepository;
import com.api.adm.repository.ProductoRepository;
import com.api.adm.service.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public CompraServiceImpl(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Compra guardarCompra(Compra compra, Long clienteId, List<CompraDetalle> detalles) {
        compra.setDetalles(detalles);
        compra.calcularTotal();
        return compraRepository.save(compra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAllWithDetalles();
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id " + id));
    }

    @Override
    public boolean eliminarCompraPorId(Long id) {
        if (compraRepository.existsById(id)) {
            Compra compra = compraRepository.findByIdWithDetalles(id).orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id " + id));
            // Incrementar el stock de cada producto en los detalles de la compra
            for (CompraDetalle detalle : compra.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
            compraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


