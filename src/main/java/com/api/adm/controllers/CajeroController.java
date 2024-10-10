package com.api.adm.controllers;

import com.api.adm.entity.Cajero;
import com.api.adm.service.CajeroService;
import com.api.adm.service.DtoConverterService;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.adm.dto.CajeroDTO;
import com.api.adm.entity.Empleado;
import java.util.stream.Collectors;


import java.util.List;

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
        Empleado empleado = empleadoService.obtenerEmpleadoPorId(cajeroDTO.getEmpleadoId());
        Cajero cajero = dtoConverterService.convertirACajero(cajeroDTO, empleado);
        cajero = cajeroService.guardarCajero(cajero);
        return dtoConverterService.convertirACajeroDTO(cajero);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cajero> obtenerCajeroPorId(@PathVariable Long id) {
        Cajero cajero = cajeroService.obtenerCajeroPorId(id);
        return ResponseEntity.ok(cajero);
    }

    @PutMapping("/{id}")
    public Cajero actualizarCajero(@PathVariable Long id, @RequestBody Cajero cajero) {
        return cajeroService.actualizarCajero(id, cajero);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCajero(@PathVariable Long id) {
        cajeroService.eliminarCajero(id);
        return ResponseEntity.noContent().build();
    }
}
