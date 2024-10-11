package com.api.adm.service.impl;

import com.api.adm.entity.Cajero;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.CajeroRepository;
import com.api.adm.service.CajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CajeroServiceImpl implements CajeroService {

    @Autowired
    private CajeroRepository cajeroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Cajero guardarCajero(Cajero cajero) {
        // Encripta la contraseña antes de guardar el cajero
        if (cajero.getPassword() != null && !cajero.getPassword().isEmpty()) {
            cajero.setPassword(passwordEncoder.encode(cajero.getPassword()));
        }
        return cajeroRepository.save(cajero);
    }

    @Override
    public List<Cajero> obtenerTodosLosCajeros() {
        return cajeroRepository.findAll();
    }

    @Override
    public List<Cajero> buscarCajeros(String query) {
        // Utiliza el método del repositorio para buscar por nombre o apellido del empleado
        return cajeroRepository.findByEmpleadoNombreContainingIgnoreCaseOrEmpleadoApellidoContainingIgnoreCase(query, query);
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
        cajero.setPermisos(cajeroDetalles.getPermisos());
        cajero.setEmpleado(cajeroDetalles.getEmpleado());

        // Encripta la contraseña solo si ha sido proporcionada
        if (cajeroDetalles.getPassword() != null && !cajeroDetalles.getPassword().isEmpty()) {
            cajero.setPassword(passwordEncoder.encode(cajeroDetalles.getPassword()));
        }

        return cajeroRepository.save(cajero);
    }

    @Override
    public void eliminarCajero(Long id) {
        Cajero cajero = obtenerCajeroPorId(id);
        cajeroRepository.delete(cajero);
    }
}





