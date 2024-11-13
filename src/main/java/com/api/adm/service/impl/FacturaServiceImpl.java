package com.api.adm.service.impl;

import com.api.adm.entity.Factura;
import com.api.adm.entity.FacturaDetalle;
import com.api.adm.repository.FacturaRepository;
import com.api.adm.service.FacturaService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con el ID: " + id));
    }

    @Override
    public Factura crearFactura(Factura factura, Long clienteId, List<FacturaDetalle> detalles) {
        factura.setDetalles(detalles);
        factura.calcularTotal();
        return facturaRepository.save(factura);
    }

    @Override
    public Factura actualizarFactura(Long id, Factura facturaActualizada) {
        Optional<Factura> facturaExistente = facturaRepository.findById(id);
        if (facturaExistente.isPresent()) {
            Factura factura = facturaExistente.get();
            factura.setCliente(facturaActualizada.getCliente());
            factura.setDetalles(facturaActualizada.getDetalles());
            factura.setTotal(facturaActualizada.getTotal());
            return facturaRepository.save(factura);
        } else {
            throw new IllegalArgumentException("Factura no encontrada con el ID: " + id);
        }
    }

    @Override
    public void eliminarFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Factura no encontrada con el ID: " + id);
        }
    }

    @Override
    public byte[] generarFacturaPdf(Long id) throws IOException {
        Factura factura = obtenerFacturaPorId(id);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Factura #" + factura.getNumeroFactura());
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.showText("Cliente: " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
                contentStream.newLine();
                contentStream.showText("Fecha de Emisión: " + factura.getFechaEmision());
                contentStream.newLine();
                contentStream.showText("Método de Pago: " + factura.getMetodoPago());
                contentStream.newLine();
                contentStream.showText("Estado: " + factura.getEstado());
                contentStream.newLine();
                contentStream.newLine();

                contentStream.showText("Detalles de la Factura:");
                contentStream.newLine();
                contentStream.showText("-----------------------------------------------------------");
                contentStream.newLine();
                contentStream.showText(String.format("%-20s %-10s %-10s %-10s", "Producto", "Cantidad", "Precio", "Subtotal"));
                contentStream.newLine();
                contentStream.showText("-----------------------------------------------------------");

                for (FacturaDetalle detalle : factura.getDetalles()) {
                    contentStream.newLine();
                    contentStream.showText(String.format("%-20s %-10d %-10.2f %-10.2f",
                            detalle.getProducto().getNombre(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario(),
                            detalle.getSubtotal()));
                }

                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("-----------------------------------------------------------");
                contentStream.newLine();
                contentStream.showText(String.format("Total: %.2f", factura.getTotal()));
                contentStream.endText();
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}



