// EmpleadoServiceImpl.java
package com.api.adm.service.impl;

import com.api.adm.entity.Empleado;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.EmpleadoRepository;
import com.api.adm.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
    }

    @Override
    public Empleado actualizarEmpleado(Long id, Empleado empleadoDetalles) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        empleado.setNombre(empleadoDetalles.getNombre());
        empleado.setApellido(empleadoDetalles.getApellido());
        empleado.setDni(empleadoDetalles.getDni());
        empleado.setCelular(empleadoDetalles.getCelular());
        empleado.setCorreo(empleadoDetalles.getCorreo());
        return empleadoRepository.save(empleado);
    }

    @Override
    public void eliminarEmpleado(Long id) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        empleadoRepository.delete(empleado);
    }
}

