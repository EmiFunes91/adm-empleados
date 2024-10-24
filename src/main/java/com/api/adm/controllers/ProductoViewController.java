package com.api.adm.controllers;

import com.api.adm.dto.ProductoDTO;
import com.api.adm.entity.Producto;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarProductos(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Producto> productos;
        if (query != null && !query.isEmpty()) {
            productos = productoService.buscarProductos(query);
        } else {
            productos = productoService.obtenerTodosLosProductos();
        }
        model.addAttribute("productos", productos);
        model.addAttribute("query", query);
        return "lista_productos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("productoDTO", new ProductoDTO());
        return "agregar_producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "agregar_producto";
        }
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());

        productoService.guardarProducto(producto);
        return "redirect:/productos?success=created";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setCategoria(producto.getCategoria());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        model.addAttribute("productoDTO", productoDTO);
        return "editar_producto";
    }

    @PostMapping("/editar/{id}")
    public String actualizarProducto(@PathVariable Long id, @Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editar_producto";
        }

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());

        productoService.actualizarProducto(id, producto);
        return "redirect:/productos?success=updated";
    }
}





