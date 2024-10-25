package com.api.adm.service;

import com.api.adm.dto.ProductoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.api.adm.entity.Producto;
import com.api.adm.repository.ProductoRepository;

import jakarta.validation.Valid;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Producto> buscarProductos(String query) {
        return productoRepository.findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(@Valid Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setNombre(productoActualizado.getNombre());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Page<ProductoDTO> obtenerProductosPaginados(int page, int size) {
        Page<Producto> productos = productoRepository.findAll(PageRequest.of(page, size));
        return productos.map(producto -> modelMapper.map(producto, ProductoDTO.class));
    }
}



