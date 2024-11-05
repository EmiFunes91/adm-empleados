package com.api.adm.controllers;

import com.api.adm.entity.Compra;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    // Método para mostrar la lista de todas las compras
    @GetMapping
    public String obtenerTodasLasCompras(Model model) {
        List<Compra> compras = compraService.obtenerTodasLasCompras();
        model.addAttribute("compras", compras);
        return "historial_compras";
    }

    // Método para mostrar los detalles de una compra
    @GetMapping("/detalle/{id}")
    public String obtenerCompraPorId(@PathVariable Long id, Model model) {
        Compra compra = compraService.obtenerCompraPorId(id);
        if (compra == null) {
            model.addAttribute("error", "Compra no encontrada");
            return "redirect:/compras/historial";
        }
        model.addAttribute("compra", compra);
        return "detalle_compra";
    }

    // Método para eliminar una compra
    @PostMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean eliminado = compraService.eliminarCompraPorId(id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("success", "Compra eliminada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Compra no encontrada.");
        }
        return "redirect:/compras";
    }
}


