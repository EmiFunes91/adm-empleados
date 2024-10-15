package com.api.adm.controllers;

import com.api.adm.dto.EmpleadoDTO;
import com.api.adm.entity.Empleado;
import com.api.adm.service.DtoConverterService;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private DtoConverterService dtoConverterService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<EmpleadoDTO> obtenerTodosLosEmpleados() {
        return empleadoService.obtenerTodosLosEmpleados().stream()
                .map(dtoConverterService::convertirAEmpleadoDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // Permitir que USER también agregue empleados
    public EmpleadoDTO guardarEmpleado(@RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = dtoConverterService.convertirAEmpleado(empleadoDTO);
        Empleado nuevoEmpleado = empleadoService.guardarEmpleado(empleado);
        return dtoConverterService.convertirAEmpleadoDTO(nuevoEmpleado);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<EmpleadoDTO> obtenerEmpleadoPorId(@PathVariable Long id) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        EmpleadoDTO empleadoDTO = dtoConverterService.convertirAEmpleadoDTO(empleado);
        return ResponseEntity.ok(empleadoDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede editar empleados
    public EmpleadoDTO actualizarEmpleado(@PathVariable Long id, @RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = dtoConverterService.convertirAEmpleado(empleadoDTO);
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleado);
        return dtoConverterService.convertirAEmpleadoDTO(empleadoActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar empleados
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}


