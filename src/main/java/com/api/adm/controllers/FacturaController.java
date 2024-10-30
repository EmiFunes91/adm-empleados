package com.api.adm.controllers;

import com.api.adm.entity.Factura;
import com.api.adm.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // Obtener todas las facturas
    @GetMapping
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaService.obtenerTodasLasFacturas();
    }

    // Obtener factura por ID
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerFacturaPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }

    // Crear una nueva factura
    @PostMapping
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura, @RequestParam Long clienteId) {
        Factura nuevaFactura = facturaService.crearFactura(factura, clienteId);
        return ResponseEntity.ok(nuevaFactura);
    }

    // Actualizar una factura existente
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizarFactura(@PathVariable Long id, @RequestBody Factura facturaActualizada) {
        Factura facturaActual = facturaService.actualizarFactura(id, facturaActualizada);
        return ResponseEntity.ok(facturaActual);
    }

    // Eliminar una factura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }

    // Generar PDF de una factura
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarFacturaPdf(@PathVariable Long id) throws IOException {
        byte[] pdfBytes = facturaService.generarFacturaPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}

