package com.api.adm.controllers;

import com.api.adm.entity.Factura;
import com.api.adm.entity.FacturaDetalle;
import com.api.adm.service.FacturaService;
import com.api.adm.service.ClienteService;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/facturacion")
public class FacturaViewController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodasLasFacturas());
        return "facturacion";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioFactura(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "formulario_facturas";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute("factura") Factura factura,
                                 @RequestParam("clienteId") Long clienteId,
                                 @RequestParam("detalles") List<FacturaDetalle> detalles,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            return "formulario_facturas";
        }
        facturaService.crearFactura(factura, clienteId, detalles);
        return "redirect:/facturacion?success=created";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicionFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerFacturaPorId(id);
        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "editar_facturas";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarFactura(@PathVariable Long id, @Valid @ModelAttribute("factura") Factura factura,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "editar_facturas";
        }
        facturaService.actualizarFactura(id, factura);
        return "redirect:/facturacion?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return "redirect:/facturacion?success=deleted";
    }
}

