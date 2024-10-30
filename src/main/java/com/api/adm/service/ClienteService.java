package com.api.adm.service;

import com.api.adm.entity.Cliente;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    // Obtener cliente por ID
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    // Crear un nuevo cliente
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Actualizar un cliente existente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerClientePorId(id);
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setCorreo(clienteActualizado.getCorreo()); // Usamos setCorreo en lugar de setEmail
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        return clienteRepository.save(clienteExistente);
    }

    // Eliminar un cliente
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
    }
}



