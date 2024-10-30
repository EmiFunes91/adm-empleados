package com.api.adm.service;

import com.api.adm.entity.Factura;
import com.api.adm.entity.Cliente;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.FacturaRepository;
import com.api.adm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todas las facturas
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaRepository.findAll();
    }

    // Obtener factura por ID
    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura con ID " + id + " no encontrada"));
    }

    // Crear una nueva factura
    public Factura crearFactura(Factura factura, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado"));
        factura.setCliente(cliente);
        return facturaRepository.save(factura);
    }

    // Actualizar una factura existente
    public Factura actualizarFactura(Long id, Factura facturaActualizada) {
        Factura facturaExistente = obtenerFacturaPorId(id);
        facturaExistente.setTotal(facturaActualizada.getTotal());  // Usamos setTotal en lugar de setMonto
        facturaExistente.setFechaEmision(facturaActualizada.getFechaEmision());  // Usamos setFechaEmision en lugar de setFecha
        return facturaRepository.save(facturaExistente);
    }

    // Eliminar una factura
    public void eliminarFactura(Long id) {
        Factura factura = obtenerFacturaPorId(id);
        facturaRepository.delete(factura);
    }

    // Generar PDF de una factura usando Apache PDFBox
    public byte[] generarFacturaPdf(Long id) throws IOException {
        Factura factura = obtenerFacturaPorId(id);

        // Crear documento PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Escribir contenido en el PDF
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Factura ID: " + factura.getId());
        contentStream.newLine();
        contentStream.showText("Cliente: " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        contentStream.newLine();
        contentStream.showText("Monto: $" + factura.getTotal());
        contentStream.newLine();
        contentStream.showText("Fecha de Emisión: " + factura.getFechaEmision().toString());
        contentStream.endText();
        contentStream.close();

        // Convertir el PDF a byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }
}


