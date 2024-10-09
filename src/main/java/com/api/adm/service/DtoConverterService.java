package com.api.adm.service;
import com.api.adm.dto.CajeroDTO;
import com.api.adm.dto.EmpleadoDTO;
import com.api.adm.entity.Cajero;
import com.api.adm.entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

