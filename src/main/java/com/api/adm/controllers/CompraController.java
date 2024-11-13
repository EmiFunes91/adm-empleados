package com.api.adm.controllers;

import com.api.adm.entity.Compra;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public List<Compra> obtenerTodasLasCompras() {
        return compraService.obtenerComprasPorRangoFecha(
                LocalDateTime.now().minusMonths(1), LocalDateTime.now()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerCompraPorId(@PathVariable Long id) {
        Compra compra = compraService.obtenerCompraPorId(id);
        return ResponseEntity.ok(compra);
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody Compra compra) {
        Compra nuevaCompra = compraService.crearCompra(compra);
        return ResponseEntity.ok(nuevaCompra);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(@PathVariable Long id, @RequestBody Compra compraActualizada) {
        Compra compraActual = compraService.actualizarCompra(id, compraActualizada);
        return ResponseEntity.ok(compraActual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}










