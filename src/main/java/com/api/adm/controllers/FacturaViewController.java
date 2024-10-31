package com.api.adm.controllers;

import com.api.adm.entity.Factura;
import com.api.adm.entity.Cliente;
import com.api.adm.service.FacturaService;
import com.api.adm.service.ClienteService;
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

    // Listar todas las facturas
    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaService.obtenerTodasLasFacturas());
        return "facturacion";  // Vista 'facturacion.html'
    }

    // Mostrar formulario para crear una nueva factura
    @GetMapping("/nueva")
    public String mostrarFormularioFactura(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "formulario_facturas";  // Vista 'formulario_facturas.html'
    }


    // Guardar una nueva factura
    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute("factura") Factura factura, @RequestParam("clienteId") Long clienteId, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "formulario_facturas";
        }
        facturaService.crearFactura(factura, clienteId);
        return "redirect:/facturacion?success=created";
    }

    // Mostrar formulario para editar una factura existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicionFactura(@PathVariable Long id, Model model) {
        Factura factura = facturaService.obtenerFacturaPorId(id);
        model.addAttribute("factura", factura);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "formulario_facturas";  // Reutiliza 'formulario_facturas.html'
    }

    // Actualizar una factura existente
    @PostMapping("/actualizar/{id}")
    public String actualizarFactura(@PathVariable Long id, @Valid @ModelAttribute("factura") Factura factura, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "formulario_facturas";
        }
        facturaService.actualizarFactura(id, factura);
        return "redirect:/facturacion?success=updated";
    }

    // Eliminar una factura
    @PostMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return "redirect:/facturacion?success=deleted";
    }
}
