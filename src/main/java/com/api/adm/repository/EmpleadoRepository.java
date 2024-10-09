package com.api.adm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.adm.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
