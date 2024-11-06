package com.api.adm.controllers;

import com.api.adm.dto.ProductoDTO;
import com.api.adm.entity.Producto;
import com.api.adm.service.ProductoService;
import com.api.adm.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DtoConverterService dtoConverterService;

    // Obtener todos los productos activos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoService.obtenerProductosActivos().stream()
                .map(dtoConverterService::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    // Búsqueda de productos activos por nombre o categoría (AJAX)
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public List<ProductoDTO> buscarProductos(@RequestParam String query) {
        return productoService.buscarProductos(query).stream()
                .map(dtoConverterService::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    // Eliminar producto (borrado lógico)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}







