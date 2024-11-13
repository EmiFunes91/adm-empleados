package com.api.adm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de la compra es obligatoria")
    private LocalDateTime fechaCompra;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de la compra es obligatorio")
    private EstadoCompra estado = EstadoCompra.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El total de la compra es obligatorio")
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "compra", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CompraDetalle> compraDetalles = new ArrayList<>();

    public Compra() {
        this.compraDetalles = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<CompraDetalle> getCompraDetalles() { return compraDetalles; }
    public void setCompraDetalles(List<CompraDetalle> compraDetalles) {
        this.compraDetalles = compraDetalles;
        calcularTotal();
    }

    public void calcularTotal() {
        this.total = compraDetalles.stream()
                .map(CompraDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void agregarDetalle(CompraDetalle detalle) {
        detalle.setCompra(this);
        compraDetalles.add(detalle);
        calcularTotal();
    }

    public void eliminarDetalle(CompraDetalle detalle) {
        detalle.setCompra(null);
        compraDetalles.remove(detalle);
        calcularTotal();
    }
}

enum EstadoCompra {
    PENDIENTE,
    COMPLETADA,
    CANCELADA
}





