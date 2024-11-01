package com.api.adm.service;

import com.api.adm.entity.Factura;
import com.api.adm.entity.Cliente;
import com.api.adm.entity.FacturaDetalle;
import com.api.adm.entity.Producto;
import com.api.adm.entity.EstadoFactura;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.FacturaRepository;
import com.api.adm.repository.ClienteRepository;
import com.api.adm.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todas las facturas
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaRepository.findAll();
    }

    // Obtener factura por ID
    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura con ID " + id + " no encontrada"));
    }

    // Crear una nueva factura con detalles de productos y calcular el total
    @Transactional
    public Factura crearFactura(Factura factura, Long clienteId, List<FacturaDetalle> detalles) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado"));
        factura.setCliente(cliente);
        factura.setFechaEmision(LocalDate.now());
        factura.setEstado(EstadoFactura.NO_PAGADA);

        BigDecimal totalFactura = BigDecimal.ZERO;

        for (FacturaDetalle detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + detalle.getProducto().getId() + " no encontrado"));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Actualizar stock del producto
            producto.reducirStock(detalle.getCantidad());
            productoRepository.save(producto);

            // Configurar detalles de la factura
            detalle.setFactura(factura);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.calcularSubtotal();
            factura.agregarDetalle(detalle);

            // Sumar al total de la factura
            totalFactura = totalFactura.add(detalle.getSubtotal());
        }

        factura.setTotal(totalFactura);
        return facturaRepository.save(factura);
    }

    // Actualizar una factura existente
    public Factura actualizarFactura(Long id, Factura facturaActualizada) {
        Factura facturaExistente = obtenerFacturaPorId(id);
        facturaExistente.setTotal(facturaActualizada.getTotal());
        facturaExistente.setFechaEmision(facturaActualizada.getFechaEmision());
        return facturaRepository.save(facturaExistente);
    }

    // Eliminar una factura
    public void eliminarFactura(Long id) {
        Factura factura = obtenerFacturaPorId(id);
        facturaRepository.delete(factura);
    }

    // Generar PDF de una factura detallada usando Apache PDFBox
    public byte[] generarFacturaPdf(Long id) throws IOException {
        Factura factura = obtenerFacturaPorId(id);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(100, 700);

        // Información básica de la factura
        contentStream.showText("Factura ID: " + factura.getId());
        contentStream.newLine();
        contentStream.showText("Cliente: " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        contentStream.newLine();
        contentStream.showText("Fecha de Emisión: " + factura.getFechaEmision().toString());
        contentStream.newLine();
        contentStream.showText("Método de Pago: " + factura.getMetodoPago());
        contentStream.newLine();
        contentStream.newLine();

        // Detalles de los productos en la factura
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.showText("Productos:");
        contentStream.newLine();

        for (FacturaDetalle detalle : factura.getDetalles()) {
            contentStream.showText("Producto: " + detalle.getProducto().getNombre());
            contentStream.newLine();
            contentStream.showText("Cantidad: " + detalle.getCantidad());
            contentStream.newLine();
            contentStream.showText("Precio Unitario: $" + detalle.getPrecioUnitario());
            contentStream.newLine();
            contentStream.showText("Subtotal: $" + detalle.getSubtotal());
            contentStream.newLine();
            contentStream.newLine();
        }

        // Total de la factura
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Total: $" + factura.getTotal());
        contentStream.endText();
        contentStream.close();

        // Convertir el PDF a byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }
}




