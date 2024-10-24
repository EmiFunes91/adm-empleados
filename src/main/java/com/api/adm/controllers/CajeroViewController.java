package com.api.adm.controllers;

import com.api.adm.entity.Cajero;
import com.api.adm.service.CajeroService;
import com.api.adm.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String mostrarFormularioAsignar(Model model) {
        model.addAttribute("cajero", new Cajero());
        model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados()); // Asegúrate de pasar los empleados aquí
        return "asignar_cajero";
    }

    @PostMapping("/guardar")
    public String guardarCajero(@ModelAttribute("cajero") @Valid Cajero cajero, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
            return "asignar_cajero"; // Regresar a la vista con errores
        }

        try {
            // Asegurarse de que el empleado existe antes de guardar
            if (cajero.getEmpleado() == null || cajero.getEmpleado().getId() == null) {
                result.rejectValue("empleado", "error.cajero", "El empleado es obligatorio.");
                model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
                return "asignar_cajero"; // Regresar a la vista con error
            }

            cajeroService.guardarCajero(cajero);
            return "redirect:/cajeros?success=created";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar el cajero: " + e.getMessage());
            model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
            return "asignar_cajero"; // Regresar a la vista con el error
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCajero(@PathVariable Long id, Model model) {
        Cajero cajero = cajeroService.obtenerCajeroPorId(id);
        if (cajero == null) {
            return "redirect:/cajeros?error=notfound"; // Manejar el caso donde el cajero no se encuentra
        }
        model.addAttribute("cajero", cajero);
        model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
        return "editar_cajero"; // Vista para editar el cajero
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCajero(@PathVariable Long id, @ModelAttribute("cajero") @Valid Cajero cajero, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
            return "editar_cajero"; // Regresar a la vista con errores
        }

        try {
            cajero.setEmpleado(empleadoService.obtenerEmpleadoPorId(cajero.getEmpleado().getId()));
            cajeroService.actualizarCajero(id, cajero);
            return "redirect:/cajeros?success=updated";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al actualizar el cajero: " + e.getMessage());
            model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
            return "editar_cajero"; // Regresar a la vista con error
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCajero(@PathVariable Long id) {
        try {
            cajeroService.eliminarCajero(id);
            return "redirect:/cajeros?success=deleted";
        } catch (Exception e) {
            return "redirect:/cajeros?error=delete_failed"; // Manejar el caso donde la eliminación falla
        }
    }
}
















