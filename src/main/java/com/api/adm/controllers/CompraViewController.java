package com.api.adm.controllers;

import com.api.adm.entity.Cliente;
import com.api.adm.entity.Compra;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.service.ClienteService;
import com.api.adm.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraViewController {

    private final CompraService compraService;
    private final ClienteService clienteService;

    @Autowired
    public CompraViewController(CompraService compraService, ClienteService clienteService) {
        this.compraService = compraService;
        this.clienteService = clienteService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public String listarCompras(Model model) {
        List<Compra> compras = compraService.obtenerComprasPorRangoFecha(
                LocalDateTime.now().minusMonths(1), LocalDateTime.now()
        );

        // Forzar carga de compraDetalles para evitar LazyInitializationException
        for (Compra compra : compras) {
            compra.getCompraDetalles().size();  // Esto inicializa la colección
        }

        model.addAttribute("compras", compras);
        return "compras";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioCrearCompra(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "formulario_compra";
    }

    @PostMapping("/guardar")
    public String guardarCompra(@ModelAttribute("compra") Compra compra, @RequestParam Long clienteId) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId);
        compra.setCliente(cliente);
        compra.setFechaCompra(LocalDateTime.now());
        compraService.crearCompra(compra);
        return "redirect:/compras?success=created";
    }

    @GetMapping("/editar/{id}")
    @Transactional(readOnly = true)
    public String mostrarFormularioEditarCompra(@PathVariable Long id, Model model) {
        Compra compra = compraService.obtenerCompraPorId(id);
        if (compra == null) {
            throw new ResourceNotFoundException("Compra no encontrada");
        }

        // Forzar carga de detalles para evitar LazyInitializationException
        compra.getCompraDetalles().size();
        model.addAttribute("compra", compra);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "editar_compra";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCompra(@PathVariable Long id, @ModelAttribute("compra") Compra compraActualizada) {
        compraService.actualizarCompra(id, compraActualizada);
        return "redirect:/compras?success=updated";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return "redirect:/compras?success=deleted";
    }

    @GetMapping("/detalle/{id}")
    @Transactional(readOnly = true)
    public String verDetalleCompra(@PathVariable("id") Long id, Model model) {
        Compra compra = compraService.obtenerCompraPorIdConDetalles(id);
        if (compra == null) {
            throw new ResourceNotFoundException("Compra no encontrada");
        }

        // Forzar carga de detalles para evitar LazyInitializationException
        compra.getCompraDetalles().size();

        // Imprimir detalles en la consola para depuración
        compra.getCompraDetalles().forEach(detalle -> {
            System.out.println("Producto: " + detalle.getProducto().getNombre());
            System.out.println("Cantidad: " + detalle.getCantidad());
            System.out.println("Precio Unitario: " + detalle.getPrecioUnitario());
            System.out.println("Subtotal: " + detalle.getSubtotal());
        });

        model.addAttribute("compra", compra);
        return "detalle_compra";
    }


    @GetMapping("/historial")
    public String filtrarHistorialCompras(
            @RequestParam(value = "fechaInicio", required = false) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) LocalDate fechaFin,
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @RequestParam(value = "estado", required = false) String estado,
            Model model
    ) {
        LocalDateTime inicio = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime fin = fechaFin != null ? fechaFin.atTime(23, 59, 59) : null;
        List<Compra> compras = compraService.obtenerComprasFiltradas(inicio, fin, clienteId, estado);
        model.addAttribute("compras", compras);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "historial_compras";
    }
}





















