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

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public String obtenerTodasLasCompras(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Compra> compras = (query != null && !query.trim().isEmpty())
                ? compraService.buscarPorClienteOFecha(query)
                : compraService.obtenerTodasLasCompras();
        model.addAttribute("compras", compras);
        model.addAttribute("query", query);
        return "historial_compras";
    }

    @GetMapping("/detalle/{id}")
    public String obtenerCompraPorId(@PathVariable Long id, Model model) {
        Compra compra = compraService.obtenerCompraPorId(id);
        if (compra == null) {
            return "redirect:/compras?error=notfound";
        }
        model.addAttribute("compra", compra);
        return "detalle_compra";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            compraService.eliminarCompra(id);
            redirectAttributes.addFlashAttribute("success", "Compra eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la compra. Verifique la información.");
        }
        return "redirect:/compras";
    }
}







