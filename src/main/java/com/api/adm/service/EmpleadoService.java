package com.api.adm.service;

import com.api.adm.entity.Empleado;
import java.util.List;

public interface EmpleadoService {
    Empleado guardarEmpleado(Empleado empleado);
    List<Empleado> obtenerTodosLosEmpleados();
    Empleado obtenerEmpleadoPorId(Long id);
    Empleado actualizarEmpleado(Long id, Empleado empleado);
    void eliminarEmpleado(Long id);
}
