package com.api.adm.controllers;

import com.api.adm.entity.Producto;
import com.api.adm.entity.Compra;
import com.api.adm.entity.CompraDetalle;
import com.api.adm.service.ClienteService;
import com.api.adm.service.ProductoService;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraViewController {

    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final CompraService compraService;

    @Autowired
    public CompraViewController(ClienteService clienteService, ProductoService productoService, CompraService compraService) {
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.compraService = compraService;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "formulario_compras";
    }

    @PostMapping("/guardar")
    public String guardarCompra(@ModelAttribute("compra") Compra compra,
                                @RequestParam("clienteId") Long clienteId,
                                @RequestParam(value = "productoIds", required = false) List<Long> productoIds,
                                @RequestParam(value = "cantidades", required = false) List<Integer> cantidades,
                                RedirectAttributes redirectAttributes) {

        List<CompraDetalle> detalles = new ArrayList<>();
        for (int i = 0; i < productoIds.size(); i++) {
            Producto producto = productoService.obtenerProductoPorId(productoIds.get(i));
            int cantidadSolicitada = cantidades.get(i);
            if (cantidadSolicitada > producto.getStock()) {
                redirectAttributes.addFlashAttribute("error", "Stock insuficiente para el producto " + producto.getNombre());
                return "redirect:/compras/nueva";
            }
            productoService.reducirStock(producto.getId(), cantidadSolicitada);

            CompraDetalle detalle = new CompraDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidadSolicitada);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.calcularSubtotal();
            detalles.add(detalle);
        }

        compra.setDetalles(detalles);
        compra.setCliente(clienteService.obtenerClientePorId(clienteId));
        compra.calcularTotal();

        compraService.guardarCompra(compra, clienteId, detalles);
        redirectAttributes.addFlashAttribute("success", "Compra realizada exitosamente.");
        return "redirect:/compras";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCompra(@PathVariable Long id, Model model) {
        Compra compra = compraService.obtenerCompraPorId(id);
        if (compra != null) {
            model.addAttribute("compra", compra);
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            return "editar_compras";
        } else {
            return "redirect:/compras?error=notfound";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCompra(
            @PathVariable Long id,
            @ModelAttribute("compra") Compra compra,
            @RequestParam(value = "nuevoProductoId", required = false) Long nuevoProductoId,
            @RequestParam(value = "cantidad", required = false) Integer cantidad,
            RedirectAttributes redirectAttributes) {

        Compra compraExistente = compraService.obtenerCompraPorId(id);
        if (compraExistente == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la compra a actualizar.");
            return "redirect:/compras";
        }

        compra.getDetalles().forEach(detalle -> {
            Producto producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
            if (detalle.getCantidad() <= producto.getStock()) {
                productoService.reducirStock(producto.getId(), detalle.getCantidad());
            } else {
                redirectAttributes.addFlashAttribute("error", "Cantidad excede el stock disponible para " + producto.getNombre());
            }
        });

        if (nuevoProductoId != null && cantidad != null && cantidad > 0) {
            Producto nuevoProducto = productoService.obtenerProductoPorId(nuevoProductoId);
            if (cantidad <= nuevoProducto.getStock()) {
                CompraDetalle nuevoDetalle = new CompraDetalle();
                nuevoDetalle.setProducto(nuevoProducto);
                nuevoDetalle.setCantidad(cantidad);
                nuevoDetalle.setPrecioUnitario(nuevoProducto.getPrecio());
                nuevoDetalle.calcularSubtotal();
                compra.getDetalles().add(nuevoDetalle);
                productoService.reducirStock(nuevoProducto.getId(), cantidad);
            } else {
                redirectAttributes.addFlashAttribute("error", "Cantidad excede el stock disponible para el nuevo producto.");
            }
        }

        compra.calcularTotal();
        compraService.actualizarCompra(id, compra);
        redirectAttributes.addFlashAttribute("success", "Compra actualizada exitosamente.");
        return "redirect:/compras";
    }
}














