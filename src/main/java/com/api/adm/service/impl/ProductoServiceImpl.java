package com.api.adm.service.impl;

import com.api.adm.entity.Producto;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.ProductoRepository;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = obtenerProductoPorId(id);
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setCategoria(producto.getCategoria());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setImagenUrl(producto.getImagenUrl());
        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public void reducirStock(Long productoId, int cantidad) {
        Producto producto = obtenerProductoPorId(productoId);
        producto.reducirStock(cantidad);
        productoRepository.save(producto);
    }

    @Override
    public void aumentarStock(Long productoId, int cantidad) { // Implementación del nuevo método
        Producto producto = obtenerProductoPorId(productoId);
        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> buscarProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query);
    }
}

