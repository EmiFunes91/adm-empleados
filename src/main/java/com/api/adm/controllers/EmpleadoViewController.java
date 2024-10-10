package com.api.adm.controllers;

import com.api.adm.entity.Empleado;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoViewController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listarEmpleados(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Empleado> empleados;
        if (query != null && !query.isEmpty()) {
            empleados = empleadoService.buscarEmpleados(query);
        } else {
            empleados = empleadoService.obtenerTodosLosEmpleados();
        }
        model.addAttribute("empleados", empleados);
        model.addAttribute("query", query);
        return "empleados"; // Nombre de la plantilla Thymeleaf para listar empleados
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "formulario_empleado";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.guardarEmpleado(empleado);
        return "redirect:/empleados?success=created";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        model.addAttribute("empleado", empleado);
        return "formulario_empleado";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarEmpleado(@PathVariable Long id, @ModelAttribute("empleado") Empleado empleado) {
        empleadoService.actualizarEmpleado(id, empleado);
        return "redirect:/empleados?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return "redirect:/empleados?success=deleted";
    }
}



