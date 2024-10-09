package com.api.adm.controllers;

import com.api.adm.entity.Empleado;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleados")
public class EmpleadoViewController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.obtenerTodosLosEmpleados());
        return "empleados"; // Nombre de la plantilla Thymeleaf para listar empleados
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "formulario_empleado"; // Nombre de la plantilla Thymeleaf para agregar o editar empleados
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoService.guardarEmpleado(empleado);
        return "redirect:/empleados";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicion(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        model.addAttribute("empleado", empleado);
        return "formulario_empleado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return "redirect:/empleados";
    }
}

