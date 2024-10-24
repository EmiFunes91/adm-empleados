package com.api.adm.controllers;

import com.api.adm.dto.ProductoDTO;
import com.api.adm.entity.Producto;
import com.api.adm.service.ProductoService;
import com.api.adm.service.DtoConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
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

    // Método para obtener todos los productos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos().stream()
                .map(dtoConverterService::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    // Método para mostrar formulario de agregar producto
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public String mostrarFormularioAgregarProducto(Model model) {
        model.addAttribute("productoDTO", new ProductoDTO());
        return "agregar_producto"; // Asegúrate de que este nombre coincida con el nombre del archivo de la vista
    }

    // Método para guardar un producto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ProductoDTO guardarProducto(@RequestBody ProductoDTO productoDTO) {
        Producto producto = dtoConverterService.convertirAProducto(productoDTO);
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return dtoConverterService.convertirAProductoDTO(nuevoProducto);
    }

    // Método para obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EMPLEADO')")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        ProductoDTO productoDTO = dtoConverterService.convertirAProductoDTO(producto);
        return ResponseEntity.ok(productoDTO);
    }

    // Método para actualizar un producto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto producto = dtoConverterService.convertirAProducto(productoDTO);
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return dtoConverterService.convertirAProductoDTO(productoActualizado);
    }

    // Método para eliminar un producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}




