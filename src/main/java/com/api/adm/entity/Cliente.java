package com.api.adm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10,15}", message = "El teléfono debe tener entre 10 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String correo;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Enumerated(EnumType.STRING)
    private EstadoCliente estado = EstadoCliente.ACTIVO;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;

    // Getters y setters
    // Constructor por defecto y parametrizado


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "El nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El apellido es obligatorio") String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank(message = "El apellido es obligatorio") String apellido) {
        this.apellido = apellido;
    }

    public @NotNull(message = "El DNI es obligatorio") @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos") String getDni() {
        return dni;
    }

    public void setDni(@NotNull(message = "El DNI es obligatorio") @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos") String dni) {
        this.dni = dni;
    }

    public @NotBlank(message = "El teléfono es obligatorio") @Pattern(regexp = "\\d{10,15}", message = "El teléfono debe tener entre 10 y 15 dígitos") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@NotBlank(message = "El teléfono es obligatorio") @Pattern(regexp = "\\d{10,15}", message = "El teléfono debe tener entre 10 y 15 dígitos") String telefono) {
        this.telefono = telefono;
    }

    public @NotBlank(message = "El correo electrónico es obligatorio") @Email(message = "El correo electrónico debe tener un formato válido") String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "El correo electrónico es obligatorio") @Email(message = "El correo electrónico debe tener un formato válido") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "La dirección es obligatoria") String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank(message = "La dirección es obligatoria") String direccion) {
        this.direccion = direccion;
    }

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
}

enum EstadoCliente {
    ACTIVO,
    INACTIVO
}

