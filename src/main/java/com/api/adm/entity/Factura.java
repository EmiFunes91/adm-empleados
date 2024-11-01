package com.api.adm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de factura es obligatorio")
    @Size(max = 20, message = "El número de factura no debe exceder los 20 caracteres")
    @Column(name = "numero_factura", unique = true, length = 20)
    private String numeroFactura;

    @NotNull(message = "La fecha de emisión es obligatoria")
    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @NotNull(message = "El total de la factura es obligatorio")
    @Positive(message = "El total debe ser un valor positivo")
    @Column(name = "total", precision = 15, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoFactura estado = EstadoFactura.NO_PAGADA;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaDetalle> detalles = new ArrayList<>();

    @NotNull(message = "El método de pago es obligatorio")
    @Column(name = "metodo_pago")
    private String metodoPago;

    // Constructores
    public Factura() {
    }

    public Factura(String numeroFactura, LocalDate fechaEmision, BigDecimal total, Cliente cliente, String metodoPago) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<FacturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FacturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    // Método para agregar un detalle de factura
    public void agregarDetalle(FacturaDetalle detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }

    // Método para calcular el total basado en los detalles
    public void calcularTotal() {
        total = detalles.stream()
                .map(FacturaDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


