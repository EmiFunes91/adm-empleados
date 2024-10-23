package com.api.adm.repository;

import com.api.adm.entity.Cajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CajeroRepository extends JpaRepository<Cajero, Long> {
    List<Cajero> findByEmpleadoNombreContainingIgnoreCaseOrEmpleadoApellidoContainingIgnoreCase(String nombre, String apellido);
}


