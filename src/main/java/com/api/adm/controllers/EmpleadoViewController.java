package com.api.adm.controllers;

import com.api.adm.entity.Empleado;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoViewController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listarEmpleados(@RequestParam(required = false) String query, Model model) {
        List<Empleado> empleados;
        if (query != null && !query.trim().isEmpty()) {
            empleados = empleadoService.buscarEmpleados(query);
        } else {
            empleados = empleadoService.obtenerTodosLosEmpleados();
        }
        model.addAttribute("empleados", empleados);
        model.addAttribute("query", query); // Para mantener la consulta en el campo de búsqueda
        return "empleados";
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado()); // Inicializa un nuevo empleado
        return "formulario_empleados"; // Nombre de la plantilla para el nuevo empleado
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "formulario_empleados"; // Regresa al formulario si hay errores
        }
        try {
            empleadoService.guardarEmpleado(empleado);
            return "redirect:/empleados?success=created";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo guardar el empleado: " + e.getMessage());
            model.addAttribute("empleado", empleado); // Regresa el objeto empleado para mostrar en el formulario
            return "formulario_empleados"; // Regresa al formulario en caso de error
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarEmpleado(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        if (empleado == null) {
            return "redirect:/empleados?error=notfound"; // O manejar el error de otra manera
        }
        model.addAttribute("empleado", empleado);
        return "formulario_empleados"; // Nombre de la plantilla para editar
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, Model model) {
        try {
            empleadoService.eliminarEmpleado(id);
            return "redirect:/empleados?success=deleted";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo eliminar el empleado: " + e.getMessage());
            return "empleados"; // Regresar a la lista de empleados
        }
    }
}








