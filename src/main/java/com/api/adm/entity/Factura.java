package com.api.adm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de factura es obligatorio")
    private String numeroFactura;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "El total de la factura es obligatorio")
    @Positive(message = "El total debe ser un valor positivo")
    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado = EstadoFactura.NO_PAGADA;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Getters y setters
    // Constructor por defecto y parametrizado

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "El número de factura es obligatorio") String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(@NotNull(message = "El número de factura es obligatorio") String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public @NotNull(message = "La fecha de emisión es obligatoria") LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(@NotNull(message = "La fecha de emisión es obligatoria") LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public @NotNull(message = "El total de la factura es obligatorio") @Positive(message = "El total debe ser un valor positivo") Double getTotal() {
        return total;
    }

    public void setTotal(@NotNull(message = "El total de la factura es obligatorio") @Positive(message = "El total debe ser un valor positivo") Double total) {
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
}

enum EstadoFactura {
    PAGADA,
    NO_PAGADA
}
