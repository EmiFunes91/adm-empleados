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

        if (productoIds == null || cantidades == null || productoIds.size() != cantidades.size()) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un producto y su cantidad.");
            return "redirect:/compras/nueva";
        }

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
        return "redirect:/compras/detalle/" + compra.getId();
    }
}
















