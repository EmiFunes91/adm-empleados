package com.api.adm.controllers;

import com.api.adm.entity.Cajero;
import com.api.adm.service.CajeroService;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cajeros")
public class CajeroViewController {

    @Autowired
    private CajeroService cajeroService;

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listarCajeros(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Cajero> cajeros;
        if (query != null && !query.isEmpty()) {
            cajeros = cajeroService.buscarCajeros(query);
        } else {
            cajeros = cajeroService.obtenerTodosLosCajeros();
        }
        model.addAttribute("cajeros", cajeros);
        model.addAttribute("query", query);
        return "cajeros"; // Nombre de la plantilla Thymeleaf para listar cajeros
    }


    @GetMapping("/asignar")
    public String mostrarFormularioAsignarCajero(Model model) {
        model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
        model.addAttribute("cajero", new Cajero());
        return "asignar_cajero";
    }

    @PostMapping("/guardar")
    public String guardarCajero(@ModelAttribute("cajero") Cajero cajero) {
        cajeroService.guardarCajero(cajero);
        return "redirect:/cajeros?success=created";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCajero(@PathVariable Long id, Model model) {
        Cajero cajero = cajeroService.obtenerCajeroPorId(id);
        model.addAttribute("cajero", cajero);
        model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
        return "editar_cajero";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCajero(@PathVariable Long id, @ModelAttribute("cajero") Cajero cajero) {
        cajeroService.actualizarCajero(id, cajero);
        return "redirect:/cajeros?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCajero(@PathVariable Long id) {
        cajeroService.eliminarCajero(id);
        return "redirect:/cajeros?success=deleted";
    }
}




