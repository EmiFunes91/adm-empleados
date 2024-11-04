package com.api.adm.controllers;

import com.api.adm.dto.ProductoDTO;
import com.api.adm.entity.Producto;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
public class ProductoViewController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoViewController.class);

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
        return "productos"; // Vista para mostrar la lista de productos
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("productoDTO", new ProductoDTO());
        return "agregar_producto";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            return "redirect:/productos?error=notfound";
        }
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setCategoria(producto.getCategoria());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setImagenUrl(producto.getImagenUrl());

        model.addAttribute("productoDTO", productoDTO);
        return "editar_producto"; // Vista para editar producto
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO, BindingResult result,
                                  @RequestParam("imagen") MultipartFile imagen, Model model) {
        if (result.hasErrors()) {
            return "agregar_producto";
        }

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());

        // Manejar la imagen
        if (!imagen.isEmpty()) {
            String rutaDirectorio = "C:/Users/emili/Documents/images";
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            String rutaImagenCompleta = rutaDirectorio + "/" + nombreArchivo;

            try {
                // Crear el directorio si no existe
                File directorioImagenes = new File(rutaDirectorio);
                if (!directorioImagenes.exists()) {
                    boolean creado = directorioImagenes.mkdirs();
                    if (!creado) {
                        logger.error("No se pudo crear el directorio para las imágenes.");
                        model.addAttribute("error", "No se pudo crear el directorio para almacenar las imágenes.");
                        return "agregar_producto";
                    }
                }

                // Guardar la imagen en el sistema de archivos
                imagen.transferTo(new File(rutaImagenCompleta));

                // Guardar solo el nombre del archivo en la entidad Producto
                producto.setImagenUrl(nombreArchivo);

            } catch (IOException e) {
                logger.error("Error al guardar la imagen: {}", e.getMessage());
                model.addAttribute("error", "No se pudo guardar la imagen.");
                return "agregar_producto";
            }
        }

        productoService.guardarProducto(producto);
        return "redirect:/productos?success=created";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                     @Valid @ModelAttribute("productoDTO") ProductoDTO productoDTO,
                                     BindingResult result,
                                     @RequestParam("imagen") MultipartFile imagen,
                                     Model model) {
        if (result.hasErrors()) {
            return "editar_producto";
        }

        Producto producto = productoService.obtenerProductoPorId(id);

        if (producto == null) {
            model.addAttribute("error", "Producto no encontrado.");
            return "editar_producto";
        }

        // Actualizar los datos del producto
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());

        // Sí se ha subido una nueva imagen, reemplazar la existente
        if (!imagen.isEmpty()) {
            String rutaDirectorio = "C:/Users/emili/Documents/images";
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            String rutaImagenCompleta = rutaDirectorio + "/" + nombreArchivo;

            try {
                // Crear el directorio si no existe
                File directorioImagenes = new File(rutaDirectorio);
                if (!directorioImagenes.exists()) {
                    if (!directorioImagenes.mkdirs()) {
                        logger.error("No se pudo crear el directorio: " + rutaDirectorio);
                        model.addAttribute("error", "No se pudo crear el directorio para almacenar las imágenes.");
                        return "editar_producto";
                    }
                }

                // Guardar la nueva imagen en el sistema de archivos
                imagen.transferTo(new File(rutaImagenCompleta));

                // Guardar el nuevo nombre de archivo en la entidad Producto
                producto.setImagenUrl(nombreArchivo);

            } catch (IOException e) {
                logger.error("Error al guardar la imagen: {}", e.getMessage());
                model.addAttribute("error", "No se pudo guardar la imagen.");
                return "editar_producto";
            }
        }

        // Guardar los cambios
        productoService.actualizarProducto(id, producto);
        return "redirect:/productos?success=updated";
    }
}












