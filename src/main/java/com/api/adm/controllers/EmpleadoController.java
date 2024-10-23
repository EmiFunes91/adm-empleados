package com.api.adm.controllers;

import com.api.adm.dto.EmpleadoDTO;
import com.api.adm.entity.Empleado;
import com.api.adm.service.DtoConverterService;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<EmpleadoDTO> obtenerTodosLosEmpleados() {
        return empleadoService.obtenerTodosLosEmpleados().stream()
                .map(dtoConverterService::convertirAEmpleadoDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public EmpleadoDTO guardarEmpleado(@RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = dtoConverterService.convertirAEmpleado(empleadoDTO);
        Empleado nuevoEmpleado = empleadoService.guardarEmpleado(empleado);
        return dtoConverterService.convertirAEmpleadoDTO(nuevoEmpleado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> obtenerEmpleadoPorId(@PathVariable Long id) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
        EmpleadoDTO empleadoDTO = dtoConverterService.convertirAEmpleadoDTO(empleado);
        return ResponseEntity.ok(empleadoDTO);
    }

    @PutMapping("/{id}")
    public EmpleadoDTO actualizarEmpleado(@PathVariable Long id, @RequestBody EmpleadoDTO empleadoDTO) {
        Empleado empleado = dtoConverterService.convertirAEmpleado(empleadoDTO);
        Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleado);
        return dtoConverterService.convertirAEmpleadoDTO(empleadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}



