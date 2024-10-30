package com.api.adm.repository;

import com.api.adm.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreContainingOrApellidoContainingOrDniContaining(String nombre, String apellido, String dni);
}

