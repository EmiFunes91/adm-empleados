package com.api.adm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.adm.entity.Empleado;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
