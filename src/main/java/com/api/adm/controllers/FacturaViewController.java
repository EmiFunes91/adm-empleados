package com.api.adm.controllers;

import com.api.adm.entity.Factura;
import com.api.adm.entity.FacturaDetalle;
import com.api.adm.entity.Producto;
import com.api.adm.service.ClienteService;
import com.api.adm.service.FacturaService;
import com.api.adm.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/facturacion")
public class FacturaViewController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/nueva")
    public String mostrarFormularioFactura(Model model) {
        model.addAttribute("factura", new Factura());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("productos", productoService.obtenerTodosLosProductos()); // productos con stock incluido
        return "formulario_facturas";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute("factura") Factura factura,
                                 @RequestParam("clienteId") Long clienteId,
                                 @RequestParam("productoIds") List<Long> productoIds,
                                 @RequestParam("cantidades") List<Integer> cantidades,
                                 BindingResult result, Model model,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            return "formulario_facturas";
        }

        List<FacturaDetalle> detalles = new ArrayList<>();
        try {
            for (int i = 0; i < productoIds.size(); i++) {
                Producto producto = productoService.obtenerProductoPorId(productoIds.get(i));
                int cantidadSolicitada = cantidades.get(i);

                // Verificar stock y reducir solo si hay suficiente cantidad
                if (producto.getStock() < cantidadSolicitada) {
                    String errorMsg = "Stock insuficiente para el producto " + producto.getNombre();
                    result.rejectValue("detalles", "error.stockInsuficiente", errorMsg);
                    model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
                    model.addAttribute("productos", productoService.obtenerTodosLosProductos());
                    return "formulario_facturas";
                }

                // Reducir el stock del producto
                productoService.reducirStock(producto.getId(), cantidadSolicitada);

                // Crear detalle de factura
                FacturaDetalle detalle = new FacturaDetalle();
                detalle.setProducto(producto);
                detalle.setCantidad(cantidadSolicitada);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.calcularSubtotal();
                detalles.add(detalle);
            }

            factura.setDetalles(detalles);
            factura.calcularTotal();
            facturaService.crearFactura(factura, clienteId, detalles);
            redirectAttributes.addFlashAttribute("success", "Factura creada exitosamente.");
            return "redirect:/facturacion";

        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la factura: " + e.getMessage());
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            model.addAttribute("productos", productoService.obtenerTodosLosProductos());
            return "formulario_facturas";
        }
    }
}





