package com.api.adm.controllers;

import com.api.adm.dto.ProductoDTO;
import com.api.adm.entity.Producto;
import com.api.adm.service.ProductoService;
import com.api.adm.service.DtoConverterService;
import org.springframework.data.domain.Page;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos().stream()
                .map(dtoConverterService::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/productos")
    public Page<ProductoDTO> obtenerProductosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productoService.obtenerProductosPaginados(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ProductoDTO guardarProducto(@RequestBody ProductoDTO productoDTO) {
        Producto producto = dtoConverterService.convertirAProducto(productoDTO);
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return dtoConverterService.convertirAProductoDTO(nuevoProducto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        ProductoDTO productoDTO = dtoConverterService.convertirAProductoDTO(producto);
        return ResponseEntity.ok(productoDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto producto = dtoConverterService.convertirAProducto(productoDTO);
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return dtoConverterService.convertirAProductoDTO(productoActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}

