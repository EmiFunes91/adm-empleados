package com.api.adm.controllers;

import com.api.adm.dto.CajeroDTO;
import com.api.adm.entity.Cajero;
import com.api.adm.entity.Empleado;
import com.api.adm.service.CajeroService;
import com.api.adm.service.DtoConverterService;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cajeros")
public class CajeroController {

    @Autowired
    private CajeroService cajeroService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private DtoConverterService dtoConverterService;

    @GetMapping
    public List<CajeroDTO> obtenerCajeros() {
        return cajeroService.obtenerTodosLosCajeros().stream()
                .map(dtoConverterService::convertirACajeroDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CajeroDTO crearCajero(@RequestBody CajeroDTO cajeroDTO) {
        // Verifica que el empleado exista antes de asignarlo
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(cajeroDTO.getEmpleadoId());
        if (empleado == null) {
            throw new RuntimeException("Empleado no encontrado");
        }

        Cajero cajero = dtoConverterService.convertirACajero(cajeroDTO, empleado);
        cajero = cajeroService.guardarCajero(cajero);
        return dtoConverterService.convertirACajeroDTO(cajero);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CajeroDTO> obtenerCajeroPorId(@PathVariable Long id) {
        Cajero cajero = cajeroService.obtenerCajeroPorId(id);
        CajeroDTO cajeroDTO = dtoConverterService.convertirACajeroDTO(cajero);
        return ResponseEntity.ok(cajeroDTO);
    }

    @PutMapping("/{id}")
    public CajeroDTO actualizarCajero(@PathVariable Long id, @RequestBody CajeroDTO cajeroDTO) {
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(cajeroDTO.getEmpleadoId());
        Cajero cajero = dtoConverterService.convertirACajero(cajeroDTO, empleado);
        Cajero cajeroActualizado = cajeroService.actualizarCajero(id, cajero);
        return dtoConverterService.convertirACajeroDTO(cajeroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCajero(@PathVariable Long id) {
        cajeroService.eliminarCajero(id);
        return ResponseEntity.noContent().build();
    }
}





