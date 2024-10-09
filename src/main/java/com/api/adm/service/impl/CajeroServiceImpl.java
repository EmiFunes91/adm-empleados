package com.api.adm.service.impl;

import com.api.adm.entity.Cajero;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CajeroRepository;
import com.api.adm.service.CajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CajeroServiceImpl implements CajeroService {

    @Autowired
    private CajeroRepository cajeroRepository;

    @Override
    public Cajero guardarCajero(Cajero cajero) {
        return cajeroRepository.save(cajero);
    }

    @Override
    public List<Cajero> obtenerTodosLosCajeros() {
        return cajeroRepository.findAll();
    }

    @Override
    public Cajero obtenerCajeroPorId(Long id) {
        return cajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cajero no encontrado con id: " + id));
    }

    @Override
    public Cajero actualizarCajero(Long id, Cajero cajeroDetalles) {
        Cajero cajero = obtenerCajeroPorId(id);
        cajero.setUsername(cajeroDetalles.getUsername());
        cajero.setPassword(cajeroDetalles.getPassword());
        cajero.setPermisos(cajeroDetalles.getPermisos());
        cajero.setEmpleado(cajeroDetalles.getEmpleado());
        return cajeroRepository.save(cajero);
    }

    @Override
    public void eliminarCajero(Long id) {
        Cajero cajero = obtenerCajeroPorId(id);
        cajeroRepository.delete(cajero);
    }
}

