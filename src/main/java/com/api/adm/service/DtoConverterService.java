package com.api.adm.service;

import com.api.adm.dto.CajeroDTO;
import com.api.adm.dto.EmpleadoDTO;
import com.api.adm.dto.ProductoDTO;
import com.api.adm.dto.UsuarioDTO;  // Asegúrate de que la clase UsuarioDTO exista y esté importada
import com.api.adm.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;  // Importa Collectors para usar con Streams

@Service
public class DtoConverterService {

    @Autowired
    private ModelMapper modelMapper;

    // Convertir de Empleado a EmpleadoDTO
    public EmpleadoDTO convertirAEmpleadoDTO(Empleado empleado) {
        return modelMapper.map(empleado, EmpleadoDTO.class);
    }

    // Convertir de EmpleadoDTO a Empleado
    public Empleado convertirAEmpleado(EmpleadoDTO empleadoDTO) {
        return modelMapper.map(empleadoDTO, Empleado.class);
    }

    // Convertir de Cajero a CajeroDTO
    public CajeroDTO convertirACajeroDTO(Cajero cajero) {
        CajeroDTO cajeroDTO = modelMapper.map(cajero, CajeroDTO.class);
        cajeroDTO.setEmpleadoId(cajero.getEmpleado().getId());
        return cajeroDTO;
    }

    // Convertir de CajeroDTO a Cajero
    public Cajero convertirACajero(CajeroDTO cajeroDTO, Empleado empleado) {
        Cajero cajero = modelMapper.map(cajeroDTO, Cajero.class);
        cajero.setEmpleado(empleado);
        return cajero;
    }

    // Convertir de Usuario a UsuarioDTO
    public UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRoles(usuario.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return usuarioDTO;
    }

    // Convertir de Producto a ProductoDTO
    public ProductoDTO convertirAProductoDTO(Producto producto) {
        return modelMapper.map(producto, ProductoDTO.class);
    }

    // Convertir de ProductoDTO a Producto
    public Producto convertirAProducto(ProductoDTO productoDTO) {
        return modelMapper.map(productoDTO, Producto.class);
    }
}


