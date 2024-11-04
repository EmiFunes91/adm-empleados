package com.api.adm.service.impl;

import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import com.api.adm.entity.Cliente;
import com.api.adm.repository.CompraRepository;
import com.api.adm.service.ClienteService;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClienteService clienteService;

    @Override
    public Compra guardarCompra(Compra compra, Long clienteId, List<CompraDetalle> detalles) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        compra.setCliente(cliente);
        compra.setDetalles(detalles);
        compra.calcularTotal();
        return compraRepository.save(compra);
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
    }
}
