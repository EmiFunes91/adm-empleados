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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraViewController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CompraService compraService;

    @GetMapping("/nueva")
    public String mostrarFormularioCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "formulario_compras";
    }

    @PostMapping("/guardar")
    public String guardarCompra(@Valid @ModelAttribute("compra") Compra compra,
                                @RequestParam("clienteId") Long clienteId,
                                @RequestParam("productoIds") List<Long> productoIds,
                                @RequestParam("cantidades") List<Integer> cantidades,
                                BindingResult result, Model model,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            return "formulario_compras";
        }

        List<CompraDetalle> detalles = new ArrayList<>();
        for (int i = 0; i < productoIds.size(); i++) {
            Producto producto = productoService.obtenerProductoPorId(productoIds.get(i));
            int cantidadSolicitada = cantidades.get(i);

            if (producto.getStock() < cantidadSolicitada) {
                result.rejectValue("detalles", "error.stockInsuficiente", "Stock insuficiente para " + producto.getNombre());
                model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
                model.addAttribute("productos", productoService.obtenerTodosLosProductos());
                return "formulario_compras";
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
        compra.calcularTotal();
        compraService.guardarCompra(compra, clienteId, detalles);
        redirectAttributes.addFlashAttribute("success", "Compra realizada exitosamente.");
        return "redirect:/compras";
    }

    @GetMapping
    public String listarCompras(Model model) {
        model.addAttribute("compras", compraService.obtenerTodasLasCompras());
        return "redirect:/compras/nueva";

    }
}




